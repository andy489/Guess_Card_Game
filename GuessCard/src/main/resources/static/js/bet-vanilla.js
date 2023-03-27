const closeBtn = document.getElementById('close-button');

closeBtn.addEventListener('click', () => {
    const closeBtn = document.getElementById('confirm-message-container');
    const dimmerEffect = document.getElementById('dimmer');

    closeBtn.classList.toggle('non-visible');
    dimmerEffect.classList.toggle('non-visible');
});