"use server"
import http from "@/lib/axios";


export async function getUserProfile(target_user_id) {
    return await http
        .get('profile', {params: {target_user_id}})
        .then((res) => {
            return {
                isAllowed: true,
                data: res.data.result.data
            };
        })
        .catch((err) => {
            return {
                isAllowed: false,
                message: err.response?.data?.message
            };
        });
}

export async function getUserPostCount() {
    return await http
        .get('post/number_post')
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result.data
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        })
}

export async function getUserFriendCount() {
    return http
        .get('friend/number_of_friends')
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result.data
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        })
}

export async function getSuggestFriend(page = 1) {
    return http
        .get('friend/view_suggest', {params: {page}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        })
}

export async function getUserPost(page = 1, target_user_id) {
    return http
        .get('post', {params: {page, target_user_id}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        })
}

export async function getCommentInPost(page = 1, post_id) {
    return http
        .get('comment', {params: {page, post_id}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        })
}

export async function getReactionInPost(page = 1, post_id) {
    return http
        .get('post_reaction', {params: {page, post_id}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        })
}

export async function getNewsFeed(page = 1, pageSize = 10) {
    return await http
        .get("/newsfeed", {
            params: {
                page,
                pageSize
            }
        })
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getChildComment(parent_comment_id, post_id, page = 1) {
    return await http
        .get(`/comment/${parent_comment_id}`, {params: {post_id, page}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getFriendRequest(page = 1) {
    return await http
        .get("/friend/get_list_send_requests", {params: {page}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getReceivedFriendRequest(page = 1) {
    return await http
        .get("/friend/get_list_receive_requests", {params: {page}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getListFriend(page = 1) {
    return await http
        .get("/friend/get_list_friends", {params: {page}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getListBlock(page = 1) {
    return await http
        .get("/friend/get_list_block", {params: {page}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function searchFriend(page = 1, keyword) {
    return http
        .get("/friend/find_friend", {params: {page, keyword}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function searchUser(page = 1, keyword) {
    return http
        .get("/search", {params: {page, keyword}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getConfigs(page = 1, page_size = 10, sort_by = "code", sort_direction = "asc") {
    return await http
        .get("/admin/configs", {params: {page, page_size, sort_by, sort_direction}})
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getConfigByCode(code) {
    return await http
        .get(`/admin/configs/${code}`)
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getDashboardData() {
    return await http
        .get("/admin/dashboard")
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function exportData(minProbability, maxProbability, startDate, endDate) {
    return await http
        .get("/admin/problematic-comments/export", {
            params: {minProbability, maxProbability, startDate, endDate},
            responseType: 'arraybuffer', // Sử dụng arraybuffer thay vì blob
        })
        .then((res) => {
            return {
                isSuccessful: true,
                data: {
                    arrayBuffer: res.data, // Trả về dạng arraybuffer
                    type: res.headers['content-type'],
                    filename: getFilenameFromHeaders(res.headers) || 'problematic_comments.xlsx'
                }
            };
        })
        .catch((err) => {
            console.log(err)
            return {
                isSuccessful: false,
                message: err.response?.data?.message || "Export failed ở data.js"
            };
        });
}

// Helper function to get filename from headers
function getFilenameFromHeaders(headers) {
    const contentDisposition = headers['content-disposition'];
    if (contentDisposition) {
        const filenameMatch = contentDisposition.match(/filename="?([^"]+)"?/);
        if (filenameMatch && filenameMatch[1]) {
            return filenameMatch[1];
        }
    }
    return null;
}

export async function getProblematicComments(page = 1, minProbability, maxProbability, startDate, endDate) {
    return await http
        .get("/admin/problematic-comments", {
            params: {page, minProbability, maxProbability, startDate, endDate}
        })
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getWeeklyStats() {
    return await http
        .get("/admin/stats/weekly")
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getMonthlyStats() {
    return await http
        .get("/admin/stats/monthly")
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

// export async function getTopViolators(limit = 10) {
//     return await http
//         .get("/admin/stats/top-violators", {params: {limit}})
//         .then((res) => {
//             return {
//                 isSuccessful: true,
//                 data: res.data.result
//             };
//         })
//         .catch((err) => {
//             return {
//                 isSuccessful: false,
//                 message: err.response?.data?.message
//             };
//         });
// }

export async function getTopViolators(limit = 10, includeBanned = true, onlyBanned = false) {
    return await http
        .get("/admin/stats/top-violators", {
            params: {
                limit,
                includeBanned,
                onlyBanned
            }
        })
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function banUser(userId, shouldBan = true) {
    return await http
        .post(`/admin/users/${userId}/ban`, null, {
            params: {
                ban: shouldBan
            }
        })
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getUserProblematicComments(userId, page = 1, pageSize = 10) {
    return await http
        .get(`/admin/users/${userId}/problematic-comments`, {
            params: {
                page,
                page_size: pageSize
            }
        })
        .then((res) => {
            return {
                isSuccessful: true,
                data: {
                    content: res.data.result.data,
                    totalElements: res.data.result.pageMeta.totalElements,
                    totalPages: res.data.result.pageMeta.totalPages,
                    hasNext: res.data.result.pageMeta.hasNext,
                    hasPrev: res.data.result.pageMeta.hasPrev,
                    page: res.data.result.pageMeta.page,
                    pageSize: res.data.result.pageMeta.pageSize
                }
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getUserViolationStats(userId, startDate, endDate) {
    return await http
        .get(`/admin/users/${userId}/violation-stats`, {
            params: {
                startDate,
                endDate
            }
        })
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getBannedUsers() {
    return await http
        .get('/admin/banned-users')
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getUserWeeklyViolationStats(userId) {
    return await http
        .get(`/admin/users/${userId}/weekly-violation-stats`)
        .then((res) => {
            return {
                isSuccessful: true,
                data: {
                    userId: res.data.result.data.userId,
                    totalCount: res.data.result.data.totalCount,
                    weeklyStats: res.data.result.data.weeklyStats
                }
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function createAppeal(reason) {
    return await http
        .post(`/appeals`, { reason })
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getMyAppeals(page = 1, pageSize = 10) {
    return await http
        .get(`/appeals/my-appeals`, { params: { page, page_size: pageSize } })
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function getAppealStatus() {
    return await http
        .get(`/appeals/status`)
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

// Admin Appeal API functions
export async function getAdminAppeals(page = 1, pageSize = 10, statuses = null) {
    const params = { page, page_size: pageSize };

    // Xử lý statuses array thành string hoặc multiple parameters
    if (statuses && statuses.length > 0) {
        if (statuses.length === 1) {
            params.statuses = statuses[0];
        } else {
            params.statuses = statuses.join(',');
        }
    }

    return await http
        .get(`/admin/appeals`, { params })
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}

export async function processAppeal(appealId, approved, adminResponse) {
    return await http
        .post(`/admin/appeals/${appealId}/process`, { approved, adminResponse })
        .then((res) => {
            return {
                isSuccessful: true,
                data: res.data.result
            };
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            };
        });
}