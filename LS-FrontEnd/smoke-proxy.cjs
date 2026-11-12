// Smoke test proxy.ts: cookie giả vs cookie ký đúng
const fs = require("fs");
const { spawn } = require("child_process");
const jwt = require("jsonwebtoken");

const secret = fs.readFileSync(".env.local", "utf8").match(/JWT_SECRET=(.*)/)[1].trim();
const sign = (role, opts = {}) =>
  jwt.sign({ username: "u", role }, secret, { algorithm: "HS256", issuer: "LAUNDRY", subject: "Laundry Management System", expiresIn: 1800, ...opts });
const forged = (role) =>
  jwt.sign({ username: "u", role }, "not-the-real-secret-not-the-real-secret-1234", { algorithm: "HS256", issuer: "LAUNDRY", expiresIn: 1800 });
const noneAlg = `${Buffer.from('{"alg":"none"}').toString("base64url")}.${Buffer.from(JSON.stringify({ role: "Admin", iss: "LAUNDRY" })).toString("base64url")}.`;

const get = async (path, cookie) => {
  const r = await fetch("http://localhost:3000" + path, { redirect: "manual", headers: cookie ? { Cookie: cookie } : {} });
  return `${r.status} -> ${r.headers.get("location") ?? "(không redirect)"}`;
};

(async () => {
  const next = spawn("npx", ["next", "start", "-p", "3000"], { shell: true, stdio: "ignore" });
  for (let i = 0; i < 60; i++) { try { await fetch("http://localhost:3000/"); break; } catch { await new Promise((r) => setTimeout(r, 1000)); } }
  const rt = sign("Admin", { expiresIn: 604800 });
  const rows = [
    ["Chưa đăng nhập vào /manage/admin", await get("/manage/admin")],
    ["Cookie GIẢ (ký sai secret) role Admin", await get("/manage/admin", `accessToken=${forged("Admin")}; refreshToken=${rt}`)],
    ["Cookie GIẢ alg=none role Admin", await get("/manage/admin", `accessToken=${noneAlg}; refreshToken=${rt}`)],
    ["Cookie giả, đã qua 1 lần refresh (_rt=1)", await get("/manage/admin?_rt=1", `accessToken=${forged("Admin")}; refreshToken=${rt}`)],
    ["Token THẬT role Admin vào /manage/admin", await get("/manage/admin", `accessToken=${sign("Admin")}; refreshToken=${rt}`)],
    ["Token THẬT role Customer vào /manage/admin", await get("/manage/admin", `accessToken=${sign("Customer")}; refreshToken=${rt}`)],
    ["Token THẬT role Employee vào /manage/storeowner", await get("/manage/storeowner", `accessToken=${sign("Employee")}; refreshToken=${rt}`)],
    ["Token HẾT HẠN, còn refresh", await get("/manage/admin", `accessToken=${sign("Admin", { expiresIn: -10 })}; refreshToken=${rt}`)],
  ];
  console.log(rows.map((r) => r.join("  =>  ")).join("\n"));
  spawn("taskkill", ["/pid", String(next.pid), "/T", "/F"], { shell: true });
  setTimeout(() => process.exit(0), 1500);
})();

