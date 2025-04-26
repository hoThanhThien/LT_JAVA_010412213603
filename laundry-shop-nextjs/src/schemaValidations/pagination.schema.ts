import z from "zod";

export const MetaSchema = z.object({
  page: z.number(),
  size: z.number(),
  totalElements: z.number(),
  totalPages: z.number(),
});

export type MetaType = z.TypeOf<typeof MetaSchema>;

export const NormalSchema = z.object({
  data: z.any().nullable(),
  message: z.string(),
});

export type NormalType = z.TypeOf<typeof NormalSchema>;
