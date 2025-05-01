// Create new not with popup
const popupLinks = document.querySelectorAll('#popup-link')
const body = document.querySelector('body')
const lockPadding = document.querySelectorAll('.lock-padding')

let unlock = true

const timeout = 300

// Show popup
if (popupLinks.length > 0) {
  popupLinks.forEach((popupLink) => {
    popupLink.addEventListener("click", (e) => {
      e.preventDefault()

      const popupName = popupLink.getAttribute('href').replace('#', '')
      const currentPopup = document.getElementById(popupName)
      
      popupOpen(currentPopup)
    })
  })
}


// Close popup
const popupCloseIcon = document.querySelectorAll('.close-popup')
if (popupCloseIcon.length > 0) {
  for (let index = 0; index < popupCloseIcon.length; index++) {
    const el = popupCloseIcon[index]

    el.addEventListener('click', function (e) {
      e.preventDefault()
      popupClose(el.closest('.popup'))
    })
  }
}


function popupOpen(currentPopup) {
  if (currentPopup && unlock) {
    const popupActive = document.querySelector('.popup.open')

    if (popupActive) {
      popupClose(popupActive, false)
    } else {
      bodyLock()
    }

    currentPopup.classList.add('open')
    currentPopup.addEventListener('click', function (e) {
      if (!e.target.closest('.popup__content')) {
        popupClose(e.target.closest('.popup'))
      }
    })
  }
}

function popupClose(popupActive, doUnlock = true) {
  if (unlock) {
    popupActive.classList.remove('open')

    if (doUnlock) {
      bodyUnlock()
    }
  }
}

function bodyLock() {
  const lockPaddingValue = window.innerWidth - document.querySelector('.page').offsetWidth + 'px'

  if (lockPadding.length > 0) {
    for (let index = 0; index < lockPadding.length; index++) {
      const el = lockPadding[index]
  
      el.style.paddingRight = lockPaddingValue
    }
  }

  body.style.paddingRight = lockPaddingValue
  body.classList.add('lock')

  unlock = false
  setTimeout(function () {
    unlock = true
  }, timeout)
}

function bodyUnlock() {
  setTimeout(function () {
    if (lockPadding.length > 0) {
      for (let index = 0; index < lockPadding.length; index++) {
        const el = lockPadding[index]
        el.style.paddingRight = '0px'
      }
    }

    body.style.paddingRight = '0px'
    body.classList.remove('lock')
  }, timeout)

  unlock = false
  setTimeout (function () {
    unlock = true
  }, timeout)
}

document.addEventListener("keydown", (e) => {
  if (e.key === 'Escape') {
    const popupActive = document.querySelector('.popup.open')
    popupClose(popupActive)
  }
})


// Creating new note after submiting
const notesContainer = document.querySelector("#notes-container")
const createNoteBtn = document.querySelector("#create-note")

createNoteBtn.addEventListener("click", (e) => {
  const userNoteName = document.querySelector("#user__note-name")
  const userNoteLogin = document.querySelector("#user__note-login")
  const userNoteEmail = document.querySelector("#user__note-email")
  const userNotePassword = document.querySelector("#user__note-password")

  if (!userNoteName.value.trim()) return;

  const newNote = document.createElement('button')
  const newNoteHeader = document.createElement('header')
  const newNoteTitle = document.createElement('h3')

  newNote.classList.add('note', 'note-wrap') // Adds classes to note (button) element

  newNoteTitle.textContent = userNoteName.value // Show note name
  userNoteName.value = "" // Clear input field after creating
  newNoteTitle.classList.add('note__header-title')
  
  const svgHelpIcon = `
    <svg width="20" height="25" viewBox="0 0 20 25" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M8.75 18.75H11.25V11.25H8.75V18.75ZM10 8.75C10.3542 8.75 10.6513 8.63 10.8913 8.39C11.1313 8.15 11.2508 7.85333 11.25 7.5C11.2492 7.14667 11.1292 6.85 10.89 6.61C10.6508 6.37 10.3542 6.25 10 6.25C9.64583 6.25 9.34917 6.37 9.11 6.61C8.87083 6.85 8.75083 7.14667 8.75 7.5C8.74917 7.85333 8.86917 8.15042 9.11 8.39125C9.35083 8.63208 9.6475 8.75167 10 8.75ZM10 25C7.10417 24.2708 4.71333 22.6092 2.8275 20.015C0.941667 17.4208 -0.000832781 14.5408 5.5212e-07 11.375V3.75L10 0L20 3.75V11.375C20 14.5417 19.0575 17.4221 17.1725 20.0163C15.2875 22.6104 12.8967 24.2717 10 25ZM10 22.375C12.1667 21.6875 13.9583 20.3125 15.375 18.25C16.7917 16.1875 17.5 13.8958 17.5 11.375V5.46875L10 2.65625L2.5 5.46875V11.375C2.5 13.8958 3.20833 16.1875 4.625 18.25C6.04167 20.3125 7.83333 21.6875 10 22.375Z" fill="white" />
    </svg>
  `
  const headerTooltip = document.createElement('div')
  const tooltipTextSpan = document.createElement('span')

  headerTooltip.classList.add('tooltip', 'note__tooltip')
  tooltipTextSpan.textContent = "Data is encrypted, to display it you need to enter a code"
  tooltipTextSpan.classList.add('tooltiptext')

  headerTooltip.innerHTML = svgHelpIcon
  headerTooltip.appendChild(tooltipTextSpan)

  // Filling note header
  newNoteHeader.classList.add('note__header')
  newNoteHeader.append(newNoteTitle, headerTooltip)


  // Creating note data
  const noteDataList = document.createElement('ul')
  noteDataList.classList.add("note__data");

  const userdata = {
    login: userNoteLogin.value,
    email: userNoteEmail.value,
    password: userNotePassword.value
  }

  Object.entries(userdata).forEach(([key, value]) => {
      if (value) {
        const noteDataElement = document.createElement("li")
        noteDataElement.classList.add("note__data-wrap")

        const noteDataPlaceholder = document.createElement("span")
        noteDataPlaceholder.classList.add("placeholder")
        
        const noteUserData = document.createElement("p")
        noteUserData.classList.add("user-data")
      
        noteDataPlaceholder.textContent = key.charAt(0).toUpperCase() + key.slice(1) + ":"
        noteUserData.textContent = value
        
        noteDataElement.append(noteDataPlaceholder, noteUserData)
        noteDataList.appendChild(noteDataElement)
      }
  });

  userNoteLogin.value = "" // Clear input field after creating
  userNoteEmail.value = "" // Clear input field after creating
  userNotePassword.value = "" // Clear input field after creating


  newNote.append(newNoteHeader, noteDataList)
  
  notesContainer.insertBefore(newNote, notesContainer.lastElementChild)

  const popupActive = document.querySelector(".popup.open");
  if (popupActive) {
    popupClose(popupActive);
  }
})

const inputFields = document.querySelectorAll("#user__note-name, #user__note-login, #user__note-email, #user__note-password")

inputFields.forEach((input) => {
  input.addEventListener("keydown", (e) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      createNoteBtn.click()
    }
  })
})