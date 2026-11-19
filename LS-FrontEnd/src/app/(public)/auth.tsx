"use client";

import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { Tabs, TabsContent } from "@/components/ui/tabs";
import Image from "next/image";
import Login from "@/components/login";
import { useState } from "react";
import Register from "@/components/register";
import Otp from "@/components/otp";
import ForgotPassword from "@/components/forgot-password";
import { RegisterBodyType } from "@/schemaValidations/auth.schema";
import { useAuthStore } from "@/lib/zustand";

export default function Auth() {
  const { openAuth, setOpenAuth, isAuth } = useAuthStore();
  const [activeTab, setActiveTab] = useState("login");
  const [dataRegister, setDataRegister] = useState<RegisterBodyType>(
    {} as RegisterBodyType
  );

  return (
    <>
      <Dialog
        open={isAuth ? false : openAuth}
        onOpenChange={(open: boolean) => {
          setOpenAuth(open);
          if (!open) {
            setActiveTab("login");
          }
        }}
      >
        <DialogContent
          className="border-none w-[calc(100vw-1.5rem)] sm:max-w-md max-h-[90dvh] overflow-y-auto p-5 sm:p-7 rounded-2xl sm:rounded-[24px]"
          aria-describedby={undefined}
        >
          <div className="flex flex-col justify-center py-1 sm:py-2">
            <DialogTitle className="z-500 sm:mx-auto sm:w-full sm:max-w-sm">
              <Image
                src="/heramo-logo.png"
                alt="Logo"
                width="233"
                height="50"
                className="mx-auto"
              />
            </DialogTitle>

            <Tabs value={activeTab} onValueChange={setActiveTab}>
              <TabsContent value="login">
                <Login
                  openAuth={openAuth}
                  setOpenAuth={setOpenAuth}
                  setActiveTab={setActiveTab}
                />
              </TabsContent>
              <TabsContent value="register">
                <Register
                  openAuth={openAuth}
                  setOpenAuth={setOpenAuth}
                  setActiveTab={setActiveTab}
                  setDataRegister={setDataRegister}
                />
              </TabsContent>
              <TabsContent value="otp">
                <Otp dataRegister={dataRegister} setOpenAuth={setOpenAuth} />
              </TabsContent>
              <TabsContent value="forgot-password">
                <ForgotPassword
                  onBack={() => setActiveTab("login")}
                  onSuccess={() => setActiveTab("login")}
                />
              </TabsContent>
            </Tabs>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
