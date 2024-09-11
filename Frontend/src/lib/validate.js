import {z} from "zod";

export const loginFormSchema = z.object({
    email: z.string().email(),
    password: z.string()
    // .regex(
    //     new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*d)[A-Za-zd@$!%*.#?&_]{8,}$"),
    //     {
    //         message:
    //             "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, and one number",
    //     }
    // ),
});

export const registerFormSchema = z.object({
    email: z.string().email(),
    firstName: z.string(),
    lastName: z.string(),
    bio: z.string(),
    location: z.string(),
    gender: z.enum(['MALE', 'FEMALE', 'OTHERS']),
    work: z.string(),
    education: z.string(),
    dateOfBirth: z.string(),
    password: z.string()
        .regex(
            new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?])(?=.*\d).{8,}$/),
            {
                message:
                    "Password must contain at least 8 characters, including one uppercase letter, one lowercase letter, one number, and one special character",
            }
        ),
})

export const changePasswordFormSchema = z.object({
    currentPassword: z.string(),
    newPassword: z.string()
        .regex(
            new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?])(?=.*\d).{8,}$/),
            {
                message:
                    "Password must contain at least 8 characters, including one uppercase letter, one lowercase letter, one number, and one special character",
            }
        ),
    confirmPassword: z.string()
        .regex(
            new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?])(?=.*\d).{8,}$/),
            {
                message:
                    "Password must contain at least 8 characters, including one uppercase letter, one lowercase letter, one number, and one special character",
            }
        ),
})

export const resetPasswordFormSchema = z.object({
    resetPassword: z.string()
        .regex(
            new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?])(?=.*\d).{8,}$/),
            {
                message:
                    "Password must contain at least 8 characters, including one uppercase letter, one lowercase letter, one number, and one special character",
            }
        ),
})

export const forgotPasswordFormSchema = z.object({
    email: z.string().email(),
})
