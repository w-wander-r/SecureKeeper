// Function to limit prompt entier
export function setLimitedInput(input, maxLength) {
  if (input === "") {
    alert("Error: folder name cannot be empty!")
    return false
  } else if (input.length > maxLength) {
    alert(`Error: max folder name length — ${maxLength} symbols!`)
    return false
  }

  return true
}