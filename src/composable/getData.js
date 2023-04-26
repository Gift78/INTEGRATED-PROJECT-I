const getData = async () => {
  try {
    const res = await fetch('http://localhost:5000/data')
    if (res.ok) {
      const data = await res.json()
      return data
    } else {
      throw new Error('There is something wrong, cannot get your data!')
    }
  } catch (err) {
    console.log(err)
  }
}
export { getData }