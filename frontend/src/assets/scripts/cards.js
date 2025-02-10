import { setLimitedInput } from "./setLimitedInput.js"

// Careate new card
export function createNewCard() {
  const cardsContainer = document.querySelector("#cards_container")
  const createNewCardBtn = document.querySelector("#create__new-card")

  createNewCardBtn.addEventListener('click', () => {
    const newCard = document.createElement('button')
    newCard.classList.add('card', 'card-wrap')
    
    const cardName = document.createElement('input')
    const userEmail = document.createElement('input')
    const userPass = document.createElement('input')

    

    cardsContainer.insertBefore(newCard, cardsContainer.lastElementChild)
  })
}