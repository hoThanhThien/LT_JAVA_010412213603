import { useRouter } from "next/navigation";

export default function ButtonMessage(props: { className?: string }) {
  const router = useRouter();
  return (
    <button
      className={props.className}
      onClick={() => {
        router.push("https://www.messenger.com/t/331186783932984/");
      }}
    >
      NHẬN TƯ VẤN NGAY
    </button>
  );
}
