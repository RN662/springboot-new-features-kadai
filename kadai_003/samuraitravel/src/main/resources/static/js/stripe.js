const stripe = Stripe('pk_test_51RlOvyFpitW9zCwEANY8pMioPX2bwRfhRzLGuiDk2zM5CMQVj6k9yhRANNrVHOyEuAIPFEcE0Fox2Ju5XbxmrnOl00yNyU8aYL');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});