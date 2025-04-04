"use client";

import { useEffect, useState } from "react";

export default function YouTubeVideos() {
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  if (!mounted) {
    return null; // hoặc return một loading placeholder
  }

  return (
    <div className="flex gap-10 justify-center items-center mt-20">
      <iframe
        src="https://www.youtube.com/embed/hz7ZNd_wEAY?controls=0&showinfo=0&rel=0&autoplay=1&loop=1"
        width="40%"
        height="360px"
        className="rounded-2xl"
      />
      <iframe
        src="https://www.youtube.com/embed/-jLGktu6Mlc?controls=0&showinfo=0&rel=0&autoplay=1&loop=1"
        width="40%"
        height="360px"
        className="rounded-2xl"
      />
    </div>
  );
}
