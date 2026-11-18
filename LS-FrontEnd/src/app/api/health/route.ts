export async function GET() {
  return Response.json(
    {
      status: "UP",
      service: "frontend",
      timestamp: new Date().toISOString(),
    },
    { headers: { "Cache-Control": "no-store" } }
  );
}
