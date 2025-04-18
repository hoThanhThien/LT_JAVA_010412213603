"use client";

import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { Tabs, TabsContent } from "@/components/ui/tabs";
import Image from "next/image";
import Login from "@/components/login";
import { useEffect, useState } from "react";
import Register from "@/components/register";
import Otp from "@/components/otp";
import { RegisterBodyType } from "@/schemaValidations/auth.schema";
import { useAuthStore } from "@/lib/zustand";

export default function Auth() {
  const { openAuth, setOpenAuth, isAuth } = useAuthStore();
  const [activeTab, setActiveTab] = useState("login");
  const [dataRegister, setDataRegister] = useState<RegisterBodyType>(
    {} as RegisterBodyType
  );

  useEffect(() => {
    if (openAuth) {
      setActiveTab("login");
    }
  }, [openAuth]);

  return (
    <>
      <Dialog
        open={isAuth ? false : openAuth}
        onOpenChange={(open: boolean) => setOpenAuth(open)}
      >
        <DialogContent
          className="border-none"
          aria-describedby={undefined}
          onInteractOutside={(e) => {
            e.preventDefault();
          }}
        >
          <div className=" flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
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
            </Tabs>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
