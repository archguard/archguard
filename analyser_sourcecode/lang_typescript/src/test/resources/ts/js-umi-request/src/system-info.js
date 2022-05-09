import request from 'umi-request';

export const querySystemInfo = (data) => {
    return request(`/api/system-info`, {
        method: 'GET',
    });
};
