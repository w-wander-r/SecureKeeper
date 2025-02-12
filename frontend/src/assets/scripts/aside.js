// Change active folder event
document.body.addEventListener('click', function (event) {
  const activeFolder = event.target.closest('.aside-folder')

  if (activeFolder) {
    document.querySelectorAll('.aside-folder').forEach(btn => btn.classList.remove('active'))
    activeFolder.classList.add('active')
  }
})

function setLimitedInput(input, maxLength) {
  if (input === "") {
    alert("Error: folder name cannot be empty!")
    return false
  } else if (input.length > maxLength) {
    alert(`Error: max folder name length — ${maxLength} symbols!`)
    return false
  }

  return true
}


// Create new folder event
const parent = document.querySelector('#aside-folders')
  const createNewFolderBtn = document.querySelector('.aside-create__new-folder')

  createNewFolderBtn.addEventListener('click', () => {
  const newFolder = document.createElement('button') // Creating new button element

  const folderIcon = document.createElement('img') // Creating folder icon

  const span = document.createElement('span') // Creating span
  var folderName = prompt("Enter a folder name:") // Input folder name
  if (setLimitedInput(folderName, 10) === false) {
    return false
  } else {
  const lastItem = parent.lastElementChild

  folderIcon.src = "../src/assets/images/sidebar/folder-icon.svg" // Path to icon
  folderIcon.classList.add('folder__icon') // Class for icon


  span.textContent = folderName // User folder name
  span.classList.add('folder__name')


  newFolder.append(folderIcon, span) // Appending icon and folder name to button
  newFolder.classList.add('aside-folder', 'aside-item')

  parent.insertBefore(newFolder, lastItem,)
  }
})