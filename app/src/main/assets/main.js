 function volumeUp() {
         const body = document.body;
         const header = document.querySelector('h1');
         const welcomeMessage = document.querySelector('.welcome-message');

         // Change the background color of the body
         body.style.background = 'linear-gradient(90deg, rgba(2,0,36,1) 0%, rgba(92,90,170,1) 70%, rgba(255,164,0,1) 100%)';

         // Change the color of the welcome message
         welcomeMessage.style.color = 'rgba(92,90,170,0.8)'; // Similar color to background

         console.log("Volume Up");
     }

     function volumeDown() {
         const body = document.body;
         const header = document.querySelector('h1');
         const welcomeMessage = document.querySelector('.welcome-message');

         // Change the background color of the body
         body.style.background = 'linear-gradient(90deg, rgba(2,0,36,1) 0%, rgba(91,170,90,1) 79%, rgba(255,164,0,1) 100%)';

         // Change the color of the welcome message
         welcomeMessage.style.color = 'rgba(91,170,90,0.8)'; // Similar color to background

         console.log("Volume Down");
     }
    function callYoutube() {
        try {
            Android.callYoutube();
            console.log("Called YouTube");
        } catch (e) {
            console.error("Error calling YouTube:", e);
        }
    }

    function callHTML() {
        try {
            Android.callHTML();
            console.log("Called HTML");
        } catch (e) {
            console.error("Error calling HTML:", e);
        }
    }

    function navigateToSetAlarm() {
                window.location.href = "set_alarm.html";
            }

      function navigateToAlarm() {
                     window.location.href = "alarm.html";
      }

document.addEventListener('keydown', function(e) {
    if (e.key === 'ArrowDown') {
        document.activeElement.nextElementSibling.focus();
    } else if (e.key === 'ArrowUp') {
        document.activeElement.previousElementSibling.focus();
    }
});



document.addEventListener('DOMContentLoaded', function() {

   setTimeout(function() {
           const splashScreen = document.querySelector('.splash-screen');
           splashScreen.style.opacity = '0';
           setTimeout(function() {
               splashScreen.style.display = 'none'; // Hide the splash screen
           }, 1000); // Match the duration of the opacity transition in CSS
       }, 3000); // Du

    function toggleSidebar() {
            const sidebar = document.querySelector('.sidebar');
            const toggleBtn = document.querySelector('.toggle-btn');

            if (sidebar.classList.contains('open')) {
                sidebar.classList.remove('open');
                sidebar.style.left = '-200px'; // Hide sidebar to the left
                toggleBtn.classList.remove('open');
            } else {
                sidebar.classList.add('open');
                sidebar.style.left = '0'; // Show sidebar
                toggleBtn.classList.add('open');
            }
        }


    document.querySelector('.toggle-btn').addEventListener('click', toggleSidebar);

    const searchBar = document.querySelector('.search-bar');
    const sidebarButtons = document.querySelectorAll('.sidebar button');

    searchBar.addEventListener('input', function() {
        const query = searchBar.value.toLowerCase();

        sidebarButtons.forEach(button => {
            const buttonText = button.textContent.toLowerCase();
            if (buttonText.includes(query)) {
                button.style.display = 'block'; // Show button if it matches
            } else {
                button.style.display = 'none'; // Hide button if it does not match
            }
        });
    });
});
