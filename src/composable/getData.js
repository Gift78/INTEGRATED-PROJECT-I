const getAllData = async (mode) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API + `/api/announcements?mode=${mode}`
    );
    if (res.ok) {
      const data = await res.json();
      return data;
    } else if (res.status === 404) {
      throw new Error("404 Not Found");
    } else if (res.status === 500) {
      throw new Error("500 Internal Server Error");
    } else if (res.status === 503) {
      throw new Error("503 Service Unavailable");
    } else {
      throw new Error("Something went wrong.");
    }
  } catch (err) {
    console.log(err);
  }
};

const getDataById = async (id) => {
  try {
    const res = await fetch(
      import.meta.env.VITE_ROOT_API + "/api/announcements/" + id
    );
    if (res.ok) {
      const data = await res.json();
      return data;
    } else if (res.status === 404) {
      throw new Error("404 Not Found");
    } else if (res.status === 500) {
      throw new Error("500 Internal Server Error");
    } else if (res.status === 503) {
      throw new Error("503 Service Unavailable");
    } else {
      throw new Error("Something went wrong.");
    }
  } catch (err) {
    console.log(err);
  }
};

export { getAllData, getDataById };
