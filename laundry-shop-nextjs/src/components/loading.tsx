import Image from "next/image";

export default function Loading() {
  return (
    <div className="bg-black">
      <Image
        src="/heramo-gif.gif"
        alt="gif"
        width="560"
        height="720"
        className="mx-auto"
      />
    </div>
  );
}
