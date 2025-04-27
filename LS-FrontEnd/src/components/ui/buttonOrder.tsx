import { useAuthStore } from "@/lib/zustand";

export default function ButtonOrder(props: {
  className?: string;
  children: React.ReactNode;
}) {
  const { isAuth, setOpenAuth, setOpenOrder } = useAuthStore();
  return (
    <div
      className={props.className}
      onClick={() => {
        if (!isAuth) {
          setOpenAuth(true);
        } else setOpenOrder(true);
      }}
    >
      {props.children}
    </div>
  );
}
