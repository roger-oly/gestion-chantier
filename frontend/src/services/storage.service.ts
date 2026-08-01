export const saveUser = (user: any) => {
  localStorage.setItem(
    "user",
    JSON.stringify(user)
  );
};


export const getUser = () => {

  const user = localStorage.getItem("user");

  if (!user) {
    return null;
  }

  return JSON.parse(user);

};


export const removeUser = () => {

  localStorage.removeItem("user");

};