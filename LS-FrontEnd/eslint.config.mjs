import { defineConfig, globalIgnores } from "eslint/config";
import nextVitals from "eslint-config-next/core-web-vitals";
import nextTs from "eslint-config-next/typescript";
import pluginQuery from "@tanstack/eslint-plugin-query";

const eslintConfig = defineConfig([
  ...pluginQuery.configs["flat/recommended"],
  ...nextVitals,
  ...nextTs,
  {
    rules: {
      "@typescript-eslint/no-explicit-any": "off",
      "@typescript-eslint/no-unused-vars": "off",
      "@typescript-eslint/no-unused-expressions": "off",
      "@typescript-eslint/no-non-null-asserted-optional-chain": "off",
      "react-hooks/rules-of-hooks": "error",
      "react/no-unescaped-entities": "off",
      "react-hooks/exhaustive-deps": "off",
      "react-hooks/set-state-in-effect": "error",
      "react-hooks/immutability": "error",
      "@tanstack/query/exhaustive-deps": "off",
    },
  },
  globalIgnores([".next/**", "out/**", "build/**", "next-env.d.ts"]),
]);

export default eslintConfig;
