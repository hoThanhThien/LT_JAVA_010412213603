"use client";

import { ConfigProvider, Layout, Menu } from "antd";
import { ArrowLeftToLine, ArrowRightToLine } from "lucide-react";
import enUS from "antd/locale/en_US";
import { useState } from "react";
import { SiderAdmin, SiderEmployee, SiderStoreOwner } from "./menuItems";
import { usePathname } from "next/navigation";
const { Content, Sider } = Layout;
import "@ant-design/v5-patch-for-react-19";
import DropdownAvatar from "../(public)/dropdown-avatar";

export default function layout({ children }: { children: React.ReactNode }) {
  const [collapsed, setCollapsed] = useState(false);
  const [activeMenu, setActiveMenu] = useState("1");
  const pathname = usePathname();

  return (
    <>
      <ConfigProvider locale={enUS}>
        <Layout style={{ minHeight: "100vh" }}>
          <Sider
            theme="light"
            collapsible
            collapsed={collapsed}
            onCollapse={(value: any) => setCollapsed(value)}
          >
            <div style={{ height: 32, margin: 16, textAlign: "center" }}>
              Trang quản lý
            </div>
            <Menu
              selectedKeys={[activeMenu]}
              mode="inline"
              items={
                pathname.startsWith("/manage/admin")
                  ? SiderAdmin
                  : pathname.startsWith("/manage/storeowner")
                  ? SiderStoreOwner
                  : SiderEmployee
              }
              onClick={(e) => setActiveMenu(e.key)}
            />
          </Sider>
          <Layout>
            <div className="p-5 flex items-center justify-between h-[100px] border-b border-gray-200">
              <div className="flex items-center justify-between w-full">
                <div
                  className="text-sm w-16 h-16 flex items-center"
                  onClick={() => setCollapsed(!collapsed)}
                >
                  {collapsed ? <ArrowRightToLine /> : <ArrowLeftToLine />}
                </div>
                <DropdownAvatar />
              </div>
            </div>
            <Content className="p-4">{children}</Content>
          </Layout>
        </Layout>
      </ConfigProvider>
    </>
  );
}
