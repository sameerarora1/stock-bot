// Get references to the input field, send button, and chat area
const userInput = document.getElementById("stockSymbol");
const sendButton = document.getElementById("send-button");
const chatArea = document.getElementById("chat-area");

// Add a click event listener to the "Send" button
sendButton.addEventListener("click", function () {
    sendMessage();
});

// Add a keydown event listener to the input field for the "Enter" key
userInput.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        sendMessage();
    }
});

// Function to send the user's message
function sendMessage() {
    const userMessage = userInput.value;

    if (userMessage.trim() !== "") { // Check if the input is not empty
        // Create a new user message element and append it to the chat area
        const userMessageElement = document.createElement("div");
        userMessageElement.classList.add("user-message");
        userMessageElement.textContent = userMessage;
        chatArea.appendChild(userMessageElement);

        // Clear the input field
        userInput.value = "";
    }
}