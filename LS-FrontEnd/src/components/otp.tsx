import {
  InputOTP,
  InputOTPGroup,
  InputOTPSeparator,
  InputOTPSlot,
} from "@/components/ui/input-otp";
import {
  ConfirmationResult,
  RecaptchaVerifier,
  signInWithPhoneNumber,
} from "firebase/auth";
import { auth } from "../../firebase";
import { useEffect, useRef, useState, useTransition } from "react";
import { useRegisterMutation } from "@/queries/useAuth";
import { RegisterBodyType } from "@/schemaValidations/auth.schema";
import { toast } from "react-toastify";

// Hàm định dạng số điện thoại thành định dạng quốc tế
const formatPhoneNumber = (phone: string): string => {
  if (phone.startsWith("+")) return phone;
  if (phone.startsWith("0")) return `+84${phone.slice(1)}`;
  return `+${phone}`;
};

export default function Otp(props: {
  dataRegister: RegisterBodyType;
  setOpenAuth: (open: boolean) => void;
}) {
  const [otp, setOtp] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [resendCountdown, setResendCountdown] = useState(0);
  const [isOtpSent, setIsOtpSent] = useState(false);
  const [isPending, startTransition] = useTransition();

  // Refs
  const recaptchaContainerRef = useRef<HTMLDivElement>(null);
  const recaptchaVerifierRef = useRef<RecaptchaVerifier | null>(null);
  const confirmationResultRef = useRef<ConfirmationResult | null>(null);

  // Định dạng số điện thoại
  const formattedPhone = formatPhoneNumber(props.dataRegister.phone);

  const registerMutation = useRegisterMutation();

  // Đếm ngược thời gian để gửi lại OTP
  useEffect(() => {
    if (resendCountdown <= 0) return;

    const timer = setTimeout(
      () => setResendCountdown(resendCountdown - 1),
      1000
    );
    return () => clearTimeout(timer);
  }, [resendCountdown]);

  // Dọn dẹp reCAPTCHA khi component unmount
  useEffect(() => {
    return () => {
      if (recaptchaVerifierRef.current) {
        try {
          recaptchaVerifierRef.current.clear();
        } catch (error) {
          console.error("Lỗi khi xóa reCAPTCHA:", error);
        }
      }
    };
  }, []);

  // Khởi tạo gửi OTP khi component được mount
  useEffect(() => {
    if (!props.dataRegister.phone) return;

    // Kiểm tra số điện thoại hợp lệ
    const phoneRegex = /^\+?[0-9]{10,15}$/;
    if (!phoneRegex.test(props.dataRegister.phone.replace(/\s/g, ""))) {
      setError("Số điện thoại không hợp lệ. Vui lòng kiểm tra lại.");
      return;
    }

    const timer = setTimeout(() => {
      handleSendOtp();
    }, 1000);

    return () => clearTimeout(timer);
  }, [props.dataRegister.phone]);

  // Khởi tạo reCAPTCHA
  // Sửa hàm initializeRecaptcha để thêm xử lý timeout tốt hơn
  const initializeRecaptcha = () => {
    // Xóa instance cũ nếu có
    if (recaptchaVerifierRef.current) {
      try {
        recaptchaVerifierRef.current.clear();
      } catch (error) {
        console.error("Lỗi khi xóa reCAPTCHA cũ:", error);
      }
      recaptchaVerifierRef.current = null;
    }

    if (!recaptchaContainerRef.current) return false;

    // Xóa nội dung container cũ
    recaptchaContainerRef.current.innerHTML = "";

    // Tạo container mới với ID duy nhất
    const containerId = `recaptcha-container-${Date.now()}`;
    const newContainer = document.createElement("div");
    newContainer.id = containerId;
    recaptchaContainerRef.current.appendChild(newContainer);

    // Thêm promise với timeout để tránh chờ vô hạn
    return new Promise((resolve) => {
      // Timeout safety để tránh chờ quá lâu (30 giây)
      const timeoutId = setTimeout(() => {
        console.warn("reCAPTCHA initialization timed out");
        setError(
          "Không thể kết nối đến dịch vụ xác thực. Vui lòng thử lại sau."
        );
        resolve(false);
      }, 30000);

      try {
        // Khởi tạo reCAPTCHA với các cài đặt tinh chỉnh
        recaptchaVerifierRef.current = new RecaptchaVerifier(
          auth,
          containerId,
          {
            size: "invisible",
            callback: () => {
              console.log("reCAPTCHA đã xác minh");
              clearTimeout(timeoutId); // Xóa timeout khi thành công
            },
            "expired-callback": () => {
              console.log("reCAPTCHA hết hạn");
              setError("reCAPTCHA hết hạn. Vui lòng thử lại.");
            },
          }
        );

        // Render reCAPTCHA với timeout handling
        recaptchaVerifierRef.current
          .render()
          .then(() => {
            clearTimeout(timeoutId); // Xóa timeout khi thành công
            resolve(true);
          })
          .catch((error) => {
            clearTimeout(timeoutId); // Xóa timeout dù có lỗi
            console.error("Lỗi khi render reCAPTCHA:", error);
            setError("Không thể khởi tạo xác thực. Vui lòng thử lại sau.");
            resolve(false);
          });
      } catch (error: any) {
        clearTimeout(timeoutId); // Xóa timeout dù có lỗi
        console.error("Lỗi khởi tạo reCAPTCHA:", error);
        setError(error?.message || "Không thể khởi tạo reCAPTCHA");
        resolve(false);
      }
    });
  };

  // Xử lý gửi OTP
  const handleSendOtp = async () => {
    setError("");
    setIsOtpSent(false);

    startTransition(async () => {
      try {
        // Khởi tạo reCAPTCHA
        const recaptchaInitialized = await initializeRecaptcha();
        if (!recaptchaInitialized || !recaptchaVerifierRef.current) {
          setError(
            "Không thể khởi tạo reCAPTCHA. Vui lòng tải lại trang và thử lại."
          );
          return;
        }

        // Gửi OTP
        console.log("Đang gửi OTP đến:", formattedPhone);
        const result = await signInWithPhoneNumber(
          auth,
          formattedPhone,
          recaptchaVerifierRef.current
        );

        confirmationResultRef.current = result;
        console.log(result);
        setResendCountdown(60);
        setIsOtpSent(true);
        setError("");
      } catch (error: any) {
        console.error("Lỗi gửi OTP:", error);

        // Xử lý các mã lỗi cụ thể
        switch (error?.code) {
          case "auth/invalid-phone-number":
            setError(
              "Định dạng số điện thoại không hợp lệ. Vui lòng kiểm tra lại."
            );
            break;
          case "auth/too-many-requests":
            setError("Đã vượt quá giới hạn yêu cầu. Vui lòng thử lại sau.");
            break;
          case "auth/captcha-check-failed":
            setError(
              "Xác minh reCAPTCHA thất bại. Vui lòng tải lại trang và thử lại."
            );
            // Thử khởi tạo lại reCAPTCHA
            setTimeout(() => initializeRecaptcha(), 500);
            break;
          default:
            setError(
              error?.message ||
                "Không thể gửi OTP. Vui lòng kiểm tra kết nối internet và thử lại."
            );
        }
      }
    });
  };

  // Xác minh OTP
  const verifyOtp = async () => {
    if (!confirmationResultRef.current) {
      setError("Vui lòng yêu cầu gửi mã OTP trước");
      return;
    }

    if (!otp || otp.length < 6) {
      setError("Vui lòng nhập đủ mã OTP 6 chữ số");
      return;
    }

    startTransition(async () => {
      try {
        // Lưu confirmation result vào biến tạm để tránh lỗi null
        const confirmationResult = confirmationResultRef.current;

        // Kiểm tra lại để đảm bảo TypeScript không cảnh báo
        if (!confirmationResult) {
          setError(
            "Phiên xác thực đã hết hạn. Vui lòng yêu cầu gửi mã OTP mới."
          );
          return;
        }

        const result2 = await confirmationResult.confirm(otp);

        setSuccess(true);
        setError("");
        console.log("Xác minh OTP thành công!");

        if (registerMutation.isPending) return;
        try {
          const idToken = (await auth.currentUser?.getIdToken()) ?? "";
          const result = await registerMutation.mutateAsync({
            ...props.dataRegister,
            idToken,
          });
          props.setOpenAuth(false);
          toast.success(result.payload.message, {
            position: "top-right",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: false,
          });
        } catch (error: any) {
          console.error("Lỗi khi đăng ký:", error);
          setError(error?.message || "Không thể đăng ký. Vui lòng thử lại.");
        }
      } catch (error: any) {
        console.error("Lỗi khi xác minh OTP:", error);

        switch (error?.code) {
          case "auth/invalid-verification-code":
            setError("Mã xác minh không hợp lệ. Vui lòng kiểm tra và thử lại.");
            break;
          case "auth/code-expired":
            setError("Mã xác minh đã hết hạn. Vui lòng yêu cầu mã mới.");
            break;
          default:
            setError(
              error?.message || "Không thể xác minh OTP. Vui lòng thử lại."
            );
        }
      }
    });
  };

  return (
    <div>
      <div className="sm:mx-auto sm:w-full sm:max-w-sm">
        <div className="my-10 text-center">
          <div className="text-2xl/9 font-bold tracking-tight text-gray-900">
            Xác thực số điện thoại
          </div>
          <div className="mt-2 text-sm text-gray-500">
            {isOtpSent ? (
              <>
                Chúng tôi đã gửi mã OTP đến số điện thoại{" "}
                <span className="font-medium">{formattedPhone}</span>.
                <br />
                Xin vui lòng kiểm tra tin nhắn SMS và nhập mã OTP bên dưới.
              </>
            ) : (
              <>
                Đang chuẩn bị gửi mã OTP đến số điện thoại{" "}
                <span className="font-medium">{formattedPhone}</span>.
              </>
            )}
          </div>
        </div>
        <div className="flex items-center justify-center">
          <InputOTP
            maxLength={6}
            value={otp}
            onChange={setOtp}
            disabled={!isOtpSent || isPending}
          >
            <InputOTPGroup>
              <InputOTPSlot index={0} />
              <InputOTPSlot index={1} />
              <InputOTPSlot index={2} />
            </InputOTPGroup>
            <InputOTPSeparator />
            <InputOTPGroup>
              <InputOTPSlot index={3} />
              <InputOTPSlot index={4} />
              <InputOTPSlot index={5} />
            </InputOTPGroup>
          </InputOTP>
        </div>
        <div className="p-5 text-center min-h-12">
          {error && <p className="text-red-500 text-sm">{error}</p>}
          {isOtpSent && !error && (
            <p className="text-green-500 text-sm">
              Mã OTP đã được gửi thành công!
            </p>
          )}
          {isPending && <p className="text-gray-500 text-sm">Đang xử lý...</p>}
          {success && otp.length === 6 && !error && !isPending && (
            <p className="text-green-500 text-sm">Xác thực thành công!</p>
          )}
        </div>
        <div className="mt-10">
          <button
            className="cursor-pointer flex w-full justify-center rounded-md bg-main px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:opacity-50 disabled:cursor-not-allowed"
            onClick={verifyOtp}
            disabled={isPending || !otp || otp.length < 6 || !isOtpSent}
          >
            {isPending ? "Đang xác nhận..." : "Xác nhận"}
          </button>
          <button
            className="mt-5 cursor-pointer flex w-full justify-center rounded-md bg-white px-3 py-1.5 text-sm/6 font-semibold text-black shadow-xs hover:bg-black hover:text-white border border-black focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-black disabled:opacity-50 disabled:cursor-not-allowed"
            onClick={handleSendOtp}
            disabled={resendCountdown > 0 || isPending}
          >
            {resendCountdown > 0
              ? `Gửi lại mã sau ${resendCountdown} giây`
              : "Gửi lại mã"}
          </button>
        </div>
      </div>
      {/* Container ẩn cho reCAPTCHA */}
      <div ref={recaptchaContainerRef} className="recaptcha-wrapper hidden" />
    </div>
  );
}
