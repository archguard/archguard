import { baseURL } from './config';
// @ts-ignore
import { SystemEvaluationHistoryType } from "../../models/system-evaluation";
// @ts-ignore
import axios from "../axios";

export function queryEvaluationList() {
  return axios<SystemEvaluationHistoryType[]>({
    baseURL,
    url: '/evaluations',
    method: "GET",
  });
}

export function queryEvaluation<T>(id: string) {
  return axios<SystemEvaluationHistoryType[]>({
    baseURL,
    url: `/evaluations/${id}`,
    method: "GET",
  });
}

export function queryEvaluationDetails<T>(id: string) {
  return axios<T>({
    baseURL,
    url: `/evaluation-details/${id}`,
    method: "GET",
  });
}
