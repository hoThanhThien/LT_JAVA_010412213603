import { useAuthStore } from "@/lib/zustand";
import { useRouter } from "next/navigation";

export default function ButtonReqAuth(props: {
  href: string;
  className?: string;
  children: React.ReactNode;
}) {
  const { isAuth, setOpenAuth } = useAuthStore();
  const router = useRouter();
  return (
    <div
      className={`${props.className} cursor-pointer`}
      onClick={() => {
        if (!isAuth) {
          setOpenAuth(true);
        } else router.push(props.href);
      }}
    >
      {props.children}
    </div>
  );
}
