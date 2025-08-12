
function legitimateFillFormAndSubmit(event) {
  const targetDataset = event.target.dataset;
  document.getElementById('legitimateUsername').value = targetDataset.username;
  document.getElementById('legitimatePassword').value = targetDataset.password;
  document.getElementById('legitimateLoginForm').submit();
}

document.addEventListener("DOMContentLoaded", function() {
  document.querySelectorAll('.LegitimateUserBtn').forEach((btn) => {
    btn.addEventListener('click', legitimateFillFormAndSubmit, false);
  });
});
