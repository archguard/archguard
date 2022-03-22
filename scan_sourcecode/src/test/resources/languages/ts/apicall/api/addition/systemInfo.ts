import axios from "../axios";
import { baseURL } from "./config";
const systemInfoApi = "/system-info";

export interface SystemInfo {
  id: number;
  systemName: string;
  repo: string[];
  repoType: "GIT" | "SVN";
  username: string;
  password: string;
  sql: string;
  scanned: "NONE" | "SCANNING" | "SCANNED" | "FAILED";
  updatedTime: number;
  badSmellThresholdSuiteId: number;
  branch: string;
}

export function querySystemInfo() {
  return axios<SystemInfo[]>({
    baseURL,
    url: systemInfoApi,
    method: "GET",
  });
}

export function updateSystemInfo(parameter: any) {
  return axios<void>({
    baseURL,
    url: systemInfoApi,
    method: "PUT",
    data: parameter,
  });
}

export function createSystemInfo(parameter: any) {
  return axios<void>({
    baseURL,
    url: systemInfoApi,
    method: "POST",
    data: parameter,
  });
}

export function deleteSystem(systemId: number) {
  return axios<void>({
    baseURL,
    url: `${systemInfoApi}/${systemId}`,
    method: "DELETE",
  });
}
