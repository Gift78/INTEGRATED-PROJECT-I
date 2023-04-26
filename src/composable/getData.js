const getData = async () => {
    try {
      const res = await fetch("http://localhost:5000/data");
  
      if (res.ok) {
        const data = await res.json();
        return data;
      } else {
        throw new Error("cannot read data");
      }
    } catch (err) {
      alert(`ERROR: cannot read data, ${err}`);
    }
  };
  
  export { getData };
  