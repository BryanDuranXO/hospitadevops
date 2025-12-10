// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-app.js";
import { getMessaging, getToken, onMessage, isSupported } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-messaging.js";

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyBo44qARfppZVIQMU39hwAchuZRwTAERos",
  authDomain: "pwahospital.firebaseapp.com",
  projectId: "pwahospital",
  storageBucket: "pwahospital.firebasestorage.app",
  messagingSenderId: "81942710592",
  appId: "1:81942710592:web:ee443a0e2a34e93ca892a5"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Utilidades para manipular el DOM (solo si existen en la página)
const $ = (sel) => document.querySelector(sel);
const log = (m) => {
  const logElement = $("#log");
  if (logElement) {
    logElement.textContent += ((logElement.textContent === "—" ? "" : "\n") + m);
  }
  console.log(m);
};

// Clave pública VAPID
const VAPID_KEY = "BH9SGliOKPEHUmcydtFbmyXEeyrmk2IdNnGFlmcKajAoiSg6Be6dmqTgWykelzwe6469rFhawo9oTOUMn3VAbQE";

let swReg = null;
let messaging = null;

// Registrar Service Worker
async function registerServiceWorker() {
  if ('serviceWorker' in navigator) {
    try {
      swReg = await navigator.serviceWorker.register('/firebase-messaging-sw.js');
      console.log('Service Worker registrado:', swReg);
      
      if (swReg.installing) {
        await new Promise((resolve) => {
          swReg.installing.addEventListener('statechange', function() {
            if (this.state === 'activated') {
              resolve();
            }
          });
        });
      } else if (swReg.waiting) {
        swReg.waiting.postMessage({ type: 'SKIP_WAITING' });
        await new Promise((resolve) => {
          navigator.serviceWorker.addEventListener('controllerchange', resolve, { once: true });
        });
      }
      
      await navigator.serviceWorker.ready;
      log("Service Worker listo.");
      
    } catch (err) {
      console.error('Error registrando Service Worker:', err);
    }
  }
}

// Inicializar FCM
async function initializeFCM() {
  const supported = await isSupported();
  if (supported) {
    messaging = getMessaging(app);
    
    // Escuchar mensajes en primer plano
    onMessage(messaging, (payload) => {
      console.log("Mensaje en primer plano:", payload);
      log("Mensaje recibido:\n" + JSON.stringify(payload, null, 2));
      
      // Mostrar notificación personalizada si lo deseas
      if (Notification.permission === 'granted') {
        new Notification(payload.notification?.title || 'Nueva notificación', {
          body: payload.notification?.body || '',
          icon: payload.notification?.icon || '/icon-192.png'
        });
      }
    });
  } else {
    console.log("Este navegador no soporta FCM");
  }
}

// Función principal para obtener el token FCM
export async function getFCMToken() {
  try {
    // Verificar que el SW esté registrado
    if (!swReg) {
      await registerServiceWorker();
    }
    
    // Verificar que messaging esté inicializado
    if (!messaging) {
      await initializeFCM();
    }
    
    // Pedir permiso si no lo tenemos
    if (Notification.permission === 'default') {
      const permission = await Notification.requestPermission();
      if (permission !== 'granted') {
        console.log('Permiso de notificaciones denegado');
        return null;
      }
    }
    
    if (Notification.permission !== 'granted') {
      console.log('No hay permiso para notificaciones');
      return null;
    }
    
    // Obtener el token
    const token = await getToken(messaging, {
      vapidKey: VAPID_KEY,
      serviceWorkerRegistration: swReg,
    });
    
    if (token) {
      console.log('Token FCM obtenido:', token);
      // Guardar en localStorage
      localStorage.setItem('fcmToken', token);
      return token;
    } else {
      console.log('No se pudo obtener el token FCM');
      return null;
    }
    
  } catch (err) {
    console.error('Error al obtener token FCM:', err);
    return null;
  }
}

// Inicializar al cargar la página
registerServiceWorker();
initializeFCM();

// Si existe el botón de permisos en la página, vincularlo
document.addEventListener('DOMContentLoaded', () => {
  const btnPermission = document.getElementById('btn-permission');
  if (btnPermission) {
    const permElement = document.getElementById('perm');
    const tokenElement = document.getElementById('token');

    if (permElement) {
      permElement.textContent = Notification.permission;
    }

    btnPermission.addEventListener('click', async () => {
      const token = await getFCMToken();
      if (token && tokenElement) {
        tokenElement.textContent = token;
        if (typeof log === 'function') log('✓ Token obtenido y guardado en localStorage');
      }
    });
  }
});