// Change active folder event
document.body.addEventListener('click', (event) => {
  const activeFolder = event.target.closest('.aside__folder')

  if (activeFolder) {
    document.querySelectorAll('.aside__folder').forEach(btn => btn.classList.remove('active'))
    activeFolder.classList.add('active')
  }
})


// Create new folder event
const parent = document.getElementById('aside-folders')

document.querySelector('.aside__new-folder').addEventListener('click', () => {
  const newFolder = document.createElement('button') // Creating new button element
  newFolder.classList.add('aside__folder', 'aside__item')

  const folderIcon = document.createElement('img') // Creating folder icon
  folderIcon.src = "../src/assets/images/sidebar/folder-icon.svg"
  folderIcon.classList.add('folder__icon')

  const span = document.createElement('span') // Creating span
  span.classList.add('folder__name')

  const inputField = document.createElement('input') // Creating input field
  inputField.setAttribute('type', 'text')
  inputField.setAttribute('maxLength', '10')
  inputField.setAttribute('placeholder', 'New Folder')
  inputField.classList.add('folder__input')

  // Replace input with text when Enter is pressed
  inputField.addEventListener("keydown", (e) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      const folderName = inputField.value.trim() || "New Folder"
      span.textContent = folderName // Adding text inside span
    }
  })

  span.appendChild(inputField) // Add input to span
  newFolder.append(folderIcon, span) // Append icon and span to button
  
  parent.insertBefore(newFolder, parent.lastElementChild); // Append folder to the list
  inputField.focus() // Auto-focus input

})