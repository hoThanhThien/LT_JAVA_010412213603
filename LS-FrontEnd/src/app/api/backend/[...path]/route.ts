import { cookies } from "next/headers";
import envConfig from "@/config";
import {
  ACCESS_COOKIE,
  REFRESH_COOKIE,
  refreshAuthCookies,
} from "@/lib/server-auth";

/**
 * Proxy (BFF): trình duyệt gọi /api/backend/<path>, server Next đọc token từ cookie
 * HttpOnly rồi gắn `Authorization: Bearer` khi gọi Spring Boot.
 * Nhờ vậy token không bao giờ xuất hiện trong JavaScript phía client.
 */
async function handler(
  request: Request,
  { params }: { params: Promise<{ path: string[] }> }
) {
  const { path } = await params;
  if (path.some((segment) => segment === ".." || segment === ".")) {
    return Response.json({ message: "Đường dẫn không hợp lệ" }, { status: 400 });
  }

  const store = await cookies();
  let accessToken = store.get(ACCESS_COOKIE)?.value;
  const refreshToken = store.get(REFRESH_COOKIE)?.value;

  // Access token đã hết hạn (cookie tự mất) nhưng còn refresh token => tự làm mới
  if (!accessToken && refreshToken) {
    try {
      accessToken = await refreshAuthCookies(refreshToken);
    } catch {
      return Response.json(
        { message: "Phiên đăng nhập đã hết hạn" },
        { status: 401 }
      );
    }
  }

  const url = new URL(request.url);
  const target = `${envConfig.NEXT_PUBLIC_API_ENDPOINT}/${path.join("/")}${url.search}`;

  const headers = new Headers();
  const contentType = request.headers.get("content-type");
  if (contentType) headers.set("Content-Type", contentType);
  const accept = request.headers.get("accept");
  if (accept) headers.set("Accept", accept);
  if (accessToken) headers.set("Authorization", `Bearer ${accessToken}`);

  const hasBody = request.method !== "GET" && request.method !== "HEAD";
  const bodyBuffer = hasBody ? await request.arrayBuffer() : undefined;

  const callUpstream = (token?: string) => {
    const reqHeaders = new Headers(headers);
    if (token) reqHeaders.set("Authorization", `Bearer ${token}`);
    return fetch(target, {
      method: request.method,
      headers: reqHeaders,
      body: bodyBuffer,
      cache: "no-store",
    });
  };

  let upstream: Response;
  try {
    upstream = await callUpstream(accessToken);
    // Nếu access token bị từ chối (401) và vẫn còn refreshToken hợp lệ -> tự làm mới và thử lại 1 lần
    if (upstream.status === 401 && refreshToken) {
      try {
        const freshToken = await refreshAuthCookies(refreshToken);
        upstream = await callUpstream(freshToken);
      } catch {
        // Giữ nguyên response 401 từ upstream nếu refresh thất bại
      }
    }
  } catch {
    return Response.json(
      { message: "Không kết nối được tới server backend" },
      { status: 502 }
    );
  }

  const responseHeaders = new Headers();
  const upstreamType = upstream.headers.get("content-type");
  if (upstreamType) responseHeaders.set("Content-Type", upstreamType);
  responseHeaders.set("Cache-Control", "no-store");

  return new Response(
    upstream.status === 204 || upstream.status === 304 ? null : upstream.body,
    { status: upstream.status, headers: responseHeaders }
  );
}

export {
  handler as GET,
  handler as POST,
  handler as PUT,
  handler as PATCH,
  handler as DELETE,
};

