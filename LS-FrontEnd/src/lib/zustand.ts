import { create } from "zustand";

interface BookingPreset {
  category?: string;
  service?: string;
  phone?: string;
}

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
  preferredCategory: string;
  preferredService: string;
  draftPhone: string;
  setBookingPreset: (preset: BookingPreset) => void;
  clearBookingPreset: () => void;
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
  preferredCategory: "",
  preferredService: "",
  draftPhone: "",
  setBookingPreset: (preset: BookingPreset) =>
    set((state) => ({
      preferredCategory:
        preset.category !== undefined ? preset.category : state.preferredCategory,
      preferredService:
        preset.service !== undefined ? preset.service : state.preferredService,
      draftPhone: preset.phone !== undefined ? preset.phone : state.draftPhone,
    })),
  clearBookingPreset: () =>
    set({ preferredCategory: "", preferredService: "", draftPhone: "" }),
}));
