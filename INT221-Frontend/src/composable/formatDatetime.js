const formatDatetime = (datetime) => {
  if (!datetime) return null;

  const date = new Date(datetime);
  const options = {
    day: "numeric",
    month: "short",
    year: "numeric",
    hour: "numeric",
    minute: "numeric",
  };

  const formattedDate = date.toLocaleString("en-GB", options);
  return formattedDate;
};

export default formatDatetime;
