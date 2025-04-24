"use client";

import Link from "next/link";
import YouTubeVideos from "./youtube-videos";
import Image from "next/image";
import News from "./news";
import ButtonMessage from "./ui/buttonMessage";

export default function Communication() {
  return (
    <div className="py-12 max-w-7xl mx-auto px-4">
      <div className="text-4xl text-center font-bold">
        Truyền thông nói gì về chúng tôi
      </div>
      <YouTubeVideos />
      <div className="relative mt-5">
        <Link
          target="_blank"
          href={
            "https://cafebiz.vn/heramo-ung-dung-giat-ui-ve-sinh-40-dat-top-3-startup-wheel-2021-20211115122448562.chn"
          }
        >
          <Image
            src={"/home/top3-startup.png"}
            alt="top3"
            width="0"
            height="0"
            sizes="100vw"
            className="w-[40%] h-[290px] mx-auto"
          />
          <Image
            src={"/home/top3-startup-text.png"}
            alt="top3-text"
            width="311"
            height="23"
            sizes="100vw"
            className="absolute left-0 right-0 bottom-8 mx-auto max-w-[90%]"
          />
        </Link>
      </div>
      <div className="mt-10">
        <News />
      </div>
      <div className="flex justify-center items-center mt-10">
        <ButtonMessage className="bg-black text-white px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%] " />
      </div>
    </div>
  );
}
