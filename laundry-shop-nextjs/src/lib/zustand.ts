import { create } from "zustand";

interface AuthState {
  isAuth: boolean;
  setIsAuth: (value: boolean) => void;
  openAuth: boolean;
  setOpenAuth: (value: boolean) => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  isAuth: false,
  setIsAuth: (value: boolean) => set({ isAuth: value }),
  openAuth: false,
  setOpenAuth: (value: boolean) => set({ openAuth: value }),
}));
