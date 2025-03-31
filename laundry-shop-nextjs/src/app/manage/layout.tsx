"use client";

import { ConfigProvider, Layout, Menu } from "antd";
import { ArrowLeftToLine, ArrowRightToLine } from "lucide-react";
import enUS from "antd/locale/en_US";
import { useState } from "react";
import { SiderAdmin } from "./menuItems";
const { Content, Sider } = Layout;

export default function layout({ children }: { children: React.ReactNode }) {
  const [collapsed, setCollapsed] = useState(false);
  const [activeMenu, setActiveMenu] = useState("1");
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
              items={SiderAdmin}
              onClick={(e) => setActiveMenu(e.key)}
            />
          </Sider>
          <Layout>
            <div className="p-5 flex items-center justify-between h-[100px] border-b border-gray-200">
              <div
                className="text-sm w-16 h-16"
                onClick={() => setCollapsed(!collapsed)}
              >
                {collapsed ? <ArrowRightToLine /> : <ArrowLeftToLine />}
              </div>
            </div>
            <Content className="p-4">{children}</Content>
          </Layout>
        </Layout>
      </ConfigProvider>
    </>
  );
}
