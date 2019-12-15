export const USER = "ROLE_USER";
export const MODERATOR = "ROLE_MODERATOR";
export const ADMIN = "ROLE_ADMIN";

export function hasRole(role, user) {
    return user ? user.roles.includes(role) : false;
}

export function userEquals(user1, user2) {
    return (user1 && user2) ? user1.id === user2.id : false;
}