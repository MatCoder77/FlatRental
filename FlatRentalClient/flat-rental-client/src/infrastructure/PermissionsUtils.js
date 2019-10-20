export const USER = "ROLE_USER";
export const MODERATOR = "ROLE_MODERATOR";
export const ADMIN = "ROLE_ADMIN"

export function hasRole(role, user) {
    return user ? user.roles.includes(role) : false;
}