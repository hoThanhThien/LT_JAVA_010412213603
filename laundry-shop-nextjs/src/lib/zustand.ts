import { create } from "zustand";

interface AuthState {
  isAuth: boolean;
  setIsAuth: (value: boolean) => void;
  openAuth: boolean;
  setOpenAuth: (value: boolean) => void;
  openSetting: boolean;
  setOpenSetting: (value: boolean) => void;
  openOrder: boolean;
  setOpenOrder: (value: boolean) => void;
  openPay: boolean;
  setOpenPay: (value: boolean) => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  isAuth: false,
  setIsAuth: (value: boolean) => set({ isAuth: value }),
  openAuth: false,
  setOpenAuth: (value: boolean) => set({ openAuth: value }),
  openSetting: false,
  setOpenSetting: (value: boolean) => set({ openSetting: value }),
  openOrder: false,
  setOpenOrder: (value: boolean) => set({ openOrder: value }),
  openPay: false,
  setOpenPay: (value: boolean) => set({ openPay: value }),
}));
