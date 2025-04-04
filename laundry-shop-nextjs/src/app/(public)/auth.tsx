"use client";

import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { Tabs, TabsContent } from "@/components/ui/tabs";
import Image from "next/image";
import Login from "@/components/login";
import { useEffect, useState } from "react";
import Register from "@/components/register";
import Otp from "@/components/otp";

export default function Auth(props: {
  openAuth: boolean;
  setOpenAuth: (open: boolean) => void;
}) {
  const [activeTab, setActiveTab] = useState("login");
  const [phoneNumber, setPhoneNumber] = useState("");

  useEffect(() => {
    if (props.openAuth) {
      setActiveTab("login");
    }
  }, [props.openAuth]);

  return (
    <>
      <Dialog
        open={props.openAuth}
        onOpenChange={(open: boolean) => props.setOpenAuth(open)}
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
                  openAuth={props.openAuth}
                  setOpenAuth={props.setOpenAuth}
                  setActiveTab={setActiveTab}
                />
              </TabsContent>
              <TabsContent value="register">
                <Register
                  openAuth={props.openAuth}
                  setOpenAuth={props.setOpenAuth}
                  setActiveTab={setActiveTab}
                  setPhoneNumber={setPhoneNumber}
                />
              </TabsContent>
              <TabsContent value="otp">
                <Otp phoneNumber={phoneNumber} />
              </TabsContent>
            </Tabs>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
