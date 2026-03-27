# 🏆 GainZone- : Plateforme Intégrale de Gestion de Club de Sport

**GainZone-** est une solution moderne et performante conçue pour digitaliser l'ensemble des opérations d'un club de fitness. De la gestion des membres à la recommandation personnalisée de programmes via IA, GainZone centralise tout au même endroit.

---

## ✨ Fonctionnalités Clés

| Domaine | Description |
| :--- | :--- |
| **🔐 Administration** | Dashboard Analytics complet (Revenus, Membres, Ventes) avec gestion des accès RBAC (JWT). |
| **🧠 Coaching IA** | Algorithme d'analyse morphologique (Ollama) pour des recommandations d'entraînement sur mesure. |
| **🛒 E-commerce** | Boutique de suppléments intégrée avec panier dynamique et gestion des stocks. |
| **📅 Planning** | Gestion des séances, des activités et du calendrier des membres. |

---

## 🛠️ Stack Technique

### Frontend
- **Framework** : Angular 18+ (Signals, Standalone Components)
- **Styling** : Tailwind CSS
- **Icons** : Lucide Angular

### Backend
- **Langage** : Java 17
- **Framework** : Spring Boot 3.2+
- **Sécurité** : Spring Security + JJWT
- **Base de données** : PostgreSQL 15
- **IA** : Spring AI + Ollama (Modèle LlaVA)

### DevOps & Qualité
- **Conteneurisation** : Docker & Docker Compose
- **CI/CD** : GitHub Actions
- **Serveur Web** : Nginx

---

## 🚀 Installation (Docker)

Assurez-vous d'avoir [Docker](https://www.docker.com/) et [Docker Compose](https://docs.docker.com/compose/) installés sur votre machine.

1. **Cloner le projet**
   ```bash
   git clone https://github.com/YacineMARZOUK/GainZone-.git
   cd GainZone-
   ```

2. **Configurer l'environnement**
   Copiez le fichier d'exemple et adaptez les secrets si nécessaire :
   ```bash
   cp .env.example .env
   ```

3. **Lancer l'application**
   ```bash
   docker compose up --build -d
   ```

L'application sera accessible sur :
- **Frontend** : `http://localhost:80` (Reverse Proxy Nginx)
- **Backend API** : `http://localhost:8081/api`

---

## 🧪 Tests Unitaires & Qualité

La fiabilité du code est assurée par une suite de tests automatisés intégrée au pipeline CI/CD.

- **Frameworks** : JUnit 5 & Mockito.
- **Backend** : Tests des services (Logique métier) et des contrôleurs (Endpoints).
- **Exécution** :
  ```bash
  cd backend
  mvn test
  ```

---

## 📸 Aperçu de l'Interface

> [!NOTE]
> *Insérez ici vos captures d'écran pour illustrer la puissance de GainZone.*

| Dashboard | Boutique | Analyse Morphologique |
| :---: | :---: | :---: |
| ![Dashboard Placeholder](https://via.placeholder.com/300x200?text=Dashboard) | ![Shop Placeholder](https://via.placeholder.com/300x200?text=Boutique) | ![AI Placeholder](https://via.placeholder.com/300x200?text=Analyse+IA) |

---

## 👤 Auteur

- **Yacine Marzouk** - *Développeur Full-Stack & DevOps* - [GitHub](https://github.com/YacineMARZOUK)

---

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.
