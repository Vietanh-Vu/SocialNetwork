"use server";
import {cookies} from "next/headers";
import http from "./axios";

export async function refreshAccessToken(refreshToken) {
    if (refreshToken) {
        try {
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/refresh?refresh_token=${refreshToken}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({refresh_token: refreshToken}),
            });
            const data = await response.json();

            if (response.ok) {
                const newAccessToken = data.result.data.accessToken;
                const newRefreshToken = data.result.data.refreshToken;
                try {
                    // Set new access token in cookies
                    cookies().set("access-token", newAccessToken, {
                        maxAge: parseInt(process.env.NEXT_PUBLIC_ACCESS_TOKEN_EXPIRY),
                    });
                } catch (err) {
                    console.log(err)
                }

                return {
                    isSuccessful: true,
                    data: {
                        accessToken: newAccessToken,
                        refreshToken: newRefreshToken
                    }
                };
            } else {
                return {
                    isSuccessful: false,
                    message: data.message
                };
            }
        } catch (err) {
            return {
                isSuccessful: false,
                message: err.message
            };
        }
    }
    return {
        isSuccessful: false,
        message: "No refresh token provided"
    };
}

export async function login(loginForm) {
    const {email, password} = loginForm;
    console.log(email, password)
    return await http
        .post("/auth/login", {email, password})
        .then((res) => {
            const data = res.data.result.data
            cookies().set("access-token", data.accessToken, {maxAge: process.env.NEXT_PUBLIC_ACCESS_TOKEN_EXPIRY});
            cookies().set("refresh-token", data.refreshToken, {maxAge: process.env.NEXT_PUBLIC_ACCESS_REFRESH_EXPIRY});
            return {
                isSuccessful: true,
            }
        })
        .catch((err) => {
            console.log(err)
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            }
        });
}

export async function register(registerForm) {
    return await http
        .post("/auth/register", registerForm)
        .then((res) => {
            return {
                isSuccessful: true,
                message: res.data.message
            }
        })
        .catch((err) => {
            console.log(err.data)

            return {
                isSuccessful: false,
                message: err.response?.data?.message
            }
        });
}

export async function verifyRegister(token) {
    return await http
        .post(`/auth/register/verify?token=${token}`)
        .then((res) => {
            return {
                isSuccessful: true,
                message: res.data.message
            }
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            }
        });
}

export async function logout() {
    const refreshToken = cookies().get("refresh-token")?.value;
    if (refreshToken) {
        cookies().delete('refresh-token');
        cookies().delete('access-token');
        return await http
            .post("/auth/logout", {params: {refreshToken}})
            .then((res) => {
                return {
                    isSuccessful: true,
                    message: res.data.message
                }
            })
            .catch((err) => {
                return {
                    isSuccessful: false,
                    message: err.response?.data?.message
                }
            });
    }
}

export async function changePassword(new_password, old_password) {
    return await http
        .post("/auth/change_pass", null, {params: {new_password, old_password}})
        .then((res) => {
            return {
                isSuccessful: true,
            }
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            }
        });
}

export async function verifyChangePassword(token) {
    return await http
        .post(`/auth/verify_forgot_pass`, null, {params: {token}})
        .then((res) => {
            return {
                isSuccessful: true,
            }
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            }
        });
}

export async function forgotPassword(email) {
    return await http
        .post("/auth/forgot_pass", null, {params: {email}})
        .then((res) => {
            return {
                isSuccessful: true,
                message: res.data.message
            }
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            }
        });
}

export async function resetPassword(new_password, token) {
    return await http
        .post("/auth/reset_pass", null, {params: {new_password, token}})
        .then((res) => {
            return {
                isSuccessful: true,
            }
        })
        .catch((err) => {
            return {
                isSuccessful: false,
                message: err.response?.data?.message
            }
        });
}

export async function reactPost(postId) {
    return await http
        .post(`/post_reaction`, {postId: postId, reactionType: "LIKE"})
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

export async function reactComment(commentId) {
    return await http
        .post(`/comment_reaction`, {commentId: commentId, reactionType: "LIKE"})
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

export async function deleteRequestFriend(userId) {
    return await http
        .delete(`/friend/delete_request`, {params: {user_id: userId}})
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

export async function acceptRequestFriend(userId) {
    return await http
        .post(`/friend/accept_request`, null, {params: {user_id: userId}})
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

export async function rejectRequestFriend(userId) {
    return await http
        .post(`/friend/refuse_request`, null, {params: {user_id: userId}})
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

export async function unFriend(userId) {
    return await http
        .delete(`/friend/delete_friend`, {params: {user_id: userId}})
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

export async function unBlock(userId) {
    return await http
        .post(`/friend/unblock`, null, {params: {user_id: userId}})
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

export async function sendRequestFriend(userId) {
    return await http
        .post(`/friend/send_request`, null, {params: {user_id: userId}})
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

export async function block(userId) {
    return await http
        .post(`/friend/block`, null, {params: {user_id: userId}})
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

export async function setCloseRelation(userId, closeRelationship) {
    return await http
        .post(`/close_relationship`, {targetUserId: userId, closeRelationshipName: closeRelationship})
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

export async function deleteCloseRelation(target_user_id) {
    return await http
        .delete(`/close_relationship`, {params: {target_user_id}})
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

export async function createPost(formData) {
    return await http
        .post(`/post`, formData)
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

export async function deletePost(post_id) {
    return await http
        .delete(`/post`, {params: {post_id}})
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

export async function editPost(formData) {
    return await http
        .put(`/post`, formData)
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

export async function editProfile(formData) {
    return await http
        .put(`/profile`, formData)
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

export async function deleteComment(comment_id) {
    return await http
        .delete(`/comment`, {params: {comment_id}})
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

export async function editComment(formData) {
    return await http
        .put(`/comment`, formData)
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

export async function createComment(formData) {
    return await http
        .post(`/comment`, formData)
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





