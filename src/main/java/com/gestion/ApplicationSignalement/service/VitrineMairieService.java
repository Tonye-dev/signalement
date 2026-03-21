package com.gestion.ApplicationSignalement.service;

import com.gestion.ApplicationSignalement.entity.*;
import com.gestion.ApplicationSignalement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VitrineMairieService {

    @Autowired private MairieRepository               mairieRepository;
    @Autowired private ServiceMunicipalRepository     serviceMunicipalRepository;
    @Autowired private AgentMunicipalRepository       agentMunicipalRepository;
    @Autowired private AnnonceRepository              annonceRepository;
    @Autowired private NotificationRepository         notificationRepository;
    @Autowired private PreferenceNotificationRepository preferenceRepository;
    @Autowired private CitoyenRepository              citoyenRepository;
    @Autowired private AdministrateurRepository       administrateurRepository;
    @Autowired private JavaMailSender                 mailSender;

    @Value("${app.upload.dir}")            private String uploadDir;
    @Value("${app.base.url:http://localhost:8080}") private String baseUrl;
    @Value("${twilio.account.sid:}")       private String twilioSid;
    @Value("${twilio.auth.token:}")        private String twilioToken;
    @Value("${twilio.phone.from:}")        private String twilioFrom;

    // ── MAIRIES ───────────────────────────────────────────────────

    public List<Mairie> listerToutesMairies() {
        return mairieRepository.findAll();
    }

    public Mairie getMairie(Long id) {
        return mairieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mairie introuvable"));
    }

    public Mairie mettreAJourProfil(Long adminId, String description,
                                     String telephone, String emailOfficiel,
                                     String horaires, MultipartFile photo) throws IOException {
        Administrateur admin = administrateurRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));
        Mairie mairie = admin.getMairie();

        if (description   != null) mairie.setDescription(description);
        if (telephone     != null) mairie.setTelephone(telephone);
        if (emailOfficiel != null) mairie.setEmailOfficiel(emailOfficiel);
        if (horaires      != null) mairie.setHoraires(horaires);

        if (photo != null && !photo.isEmpty()) {
            Path dir = Paths.get(uploadDir + "/mairies");
            if (!Files.exists(dir)) Files.createDirectories(dir);
            String ext = photo.getOriginalFilename()
                    .substring(photo.getOriginalFilename().lastIndexOf("."));
            String nom = "mairie_" + mairie.getId() + "_" + UUID.randomUUID() + ext;
            Files.copy(photo.getInputStream(), dir.resolve(nom),
                    StandardCopyOption.REPLACE_EXISTING);
            mairie.setPhotoPath("/uploads/mairies/" + nom);
        }

        return mairieRepository.save(mairie);
    }

    // ── SERVICES MUNICIPAUX ───────────────────────────────────────

    public List<ServiceMunicipal> getServices(Long mairieId) {
        return serviceMunicipalRepository.findByMairieOrderByOrdreAsc(getMairie(mairieId));
    }

    public ServiceMunicipal ajouterService(Long adminId, String nom,
                                            String description, String icone) {
        Administrateur admin = administrateurRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));
        return serviceMunicipalRepository.save(
                new ServiceMunicipal(nom, description, icone, admin.getMairie()));
    }

    public ServiceMunicipal modifierService(Long serviceId, String nom,
                                             String description, String icone) {
        ServiceMunicipal s = serviceMunicipalRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service introuvable"));
        if (nom         != null) s.setNom(nom);
        if (description != null) s.setDescription(description);
        if (icone       != null) s.setIcone(icone);
        return serviceMunicipalRepository.save(s);
    }

    public void supprimerService(Long serviceId) {
        serviceMunicipalRepository.deleteById(serviceId);
    }

    // ── AGENTS MUNICIPAUX ─────────────────────────────────────────

    public List<AgentMunicipal> getAgents(Long mairieId) {
        return agentMunicipalRepository.findByMairieOrderByPosteAsc(getMairie(mairieId));
    }

    public AgentMunicipal ajouterAgent(Long adminId, AgentMunicipal agent,
                                        Long serviceId, MultipartFile photo) throws IOException {
        Administrateur admin = administrateurRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));
        agent.setMairie(admin.getMairie());

        if (serviceId != null)
            serviceMunicipalRepository.findById(serviceId).ifPresent(agent::setService);

        if (photo != null && !photo.isEmpty()) {
            Path dir = Paths.get(uploadDir + "/agents");
            if (!Files.exists(dir)) Files.createDirectories(dir);
            String ext = photo.getOriginalFilename()
                    .substring(photo.getOriginalFilename().lastIndexOf("."));
            String nom = "agent_" + UUID.randomUUID() + ext;
            Files.copy(photo.getInputStream(), dir.resolve(nom),
                    StandardCopyOption.REPLACE_EXISTING);
            agent.setPhotoPath("/uploads/agents/" + nom);
        }

        return agentMunicipalRepository.save(agent);
    }

    public AgentMunicipal modifierAgent(Long agentId, AgentMunicipal data,
                                         Long serviceId, MultipartFile photo) throws IOException {
        AgentMunicipal agent = agentMunicipalRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent introuvable"));

        if (data.getNom()                != null) agent.setNom(data.getNom());
        if (data.getPrenom()             != null) agent.setPrenom(data.getPrenom());
        if (data.getPoste()              != null) agent.setPoste(data.getPoste());
        if (data.getDateNaissance()      != null) agent.setDateNaissance(data.getDateNaissance());
        if (data.getDatePriseDeFonction()!= null) agent.setDatePriseDeFonction(data.getDatePriseDeFonction());
        if (serviceId != null)
            serviceMunicipalRepository.findById(serviceId).ifPresent(agent::setService);

        if (photo != null && !photo.isEmpty()) {
            Path dir = Paths.get(uploadDir + "/agents");
            if (!Files.exists(dir)) Files.createDirectories(dir);
            String ext = photo.getOriginalFilename()
                    .substring(photo.getOriginalFilename().lastIndexOf("."));
            String nom = "agent_" + agentId + "_" + UUID.randomUUID() + ext;
            Files.copy(photo.getInputStream(), dir.resolve(nom),
                    StandardCopyOption.REPLACE_EXISTING);
            agent.setPhotoPath("/uploads/agents/" + nom);
        }

        return agentMunicipalRepository.save(agent);
    }

    public void supprimerAgent(Long agentId) {
        agentMunicipalRepository.deleteById(agentId);
    }

    // ── ANNONCES ──────────────────────────────────────────────────

    public List<Annonce> getAnnonces(Long mairieId) {
        return annonceRepository.findByMairieOrderByDatePublicationDesc(getMairie(mairieId));
    }

    public List<Annonce> getToutesAnnonces() {
        return annonceRepository.findAllByOrderByDatePublicationDesc();
    }

    public Annonce publierAnnonce(Long adminId, String titre, String contenu,
                                   CategorieAnnonce categorie, boolean estArrete) {
        Administrateur admin = administrateurRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));

        Annonce annonce = new Annonce(titre, contenu, categorie, admin.getMairie(), admin);
        annonce.setEstArrete(estArrete);
        Annonce saved = annonceRepository.save(annonce);

        notifierCitoyens(saved);
        return saved;
    }

    public void supprimerAnnonce(Long annonceId, Long adminId) {
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce introuvable"));
        Administrateur admin = administrateurRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));
        if (!annonce.getMairie().getId().equals(admin.getMairie().getId()))
            throw new RuntimeException("Non autorisé");
        annonceRepository.delete(annonce);
    }

    // ── NOTIFICATIONS ─────────────────────────────────────────────

    private void notifierCitoyens(Annonce annonce) {
        List<Citoyen> tous = citoyenRepository.findAll();
        for (Citoyen citoyen : tous) {
            // In-app TOUJOURS
            String titre   = buildTitre(annonce);
            String message = buildMessage(annonce, citoyen);
            notificationRepository.save(new Notification(titre, message, citoyen, annonce));

            // Email / SMS selon préférences
            preferenceRepository.findByCitoyen(citoyen).ifPresent(pref -> {
                if (pref.getCategoriesEmail().contains(annonce.getCategorie()))
                    envoyerEmail(citoyen.getEmail(), titre, message);
                if (pref.getCategoriesSms().contains(annonce.getCategorie())
                        && citoyen.getTelephone() != null
                        && !citoyen.getTelephone().isBlank())
                    envoyerSms(citoyen.getTelephone(), titre + "\n"
                            + baseUrl + "/mairies/" + annonce.getMairie().getId() + "/annonces");
            });
        }
    }

    private String buildTitre(Annonce a) {
        return switch (a.getCategorie()) {
            case SECURITE      -> "🔒 Alerte Sécurité — " + a.getMairie().getNom();
            case TRAVAUX       -> "🚧 Travaux — " + a.getMairie().getNom();
            case EMPLOI        -> "💼 Offre d'emploi — " + a.getMairie().getNom();
            case EVENEMENTS    -> "🎭 Événement — " + a.getMairie().getNom();
            case ARRETE        -> "📋 Arrêté municipal — " + a.getMairie().getNom();
            case MARCHE_PUBLIC -> "🛒 Marché public — " + a.getMairie().getNom();
            case SANTE         -> "🏥 Santé — " + a.getMairie().getNom();
            case TRANSPORT     -> "🚌 Transport — " + a.getMairie().getNom();
            case URBANISME     -> "🏗️ Urbanisme — " + a.getMairie().getNom();
            case ENVIRONNEMENT -> "🌿 Environnement — " + a.getMairie().getNom();
            case CULTURE       -> "🎭 Culture — " + a.getMairie().getNom();
            default             -> "📢 Annonce — " + a.getMairie().getNom();
        };
    }

    private String buildMessage(Annonce a, Citoyen c) {
        String lien = baseUrl + "/mairies/" + a.getMairie().getId() + "/annonces";
        return "Bonjour " + c.getPrenom() + " " + c.getNom() + ",\n\n"
             + "La mairie de " + a.getMairie().getNom()
             + " vient de publier une annonce :\n\n"
             + "📌 " + a.getTitre() + "\n"
             + "Catégorie : " + a.getCategorie().name() + "\n\n"
             + "Consultez l'annonce complète sur ASPU :\n" + lien + "\n\n"
             + "Cordialement,\nL'équipe ASPU";
    }

    // ── PRÉFÉRENCES ───────────────────────────────────────────────

    public PreferenceNotification sauvegarderPreferences(Long citoyenId,
            Set<CategorieAnnonce> email, Set<CategorieAnnonce> sms) {
        Citoyen citoyen = citoyenRepository.findById(citoyenId)
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable"));
        PreferenceNotification pref = preferenceRepository.findByCitoyen(citoyen)
                .orElse(new PreferenceNotification(citoyen));
        pref.setCategoriesEmail(email != null ? email : new HashSet<>());
        pref.setCategoriesSms(sms    != null ? sms   : new HashSet<>());
        return preferenceRepository.save(pref);
    }

    public Optional<PreferenceNotification> getPreferences(Long citoyenId) {
        Citoyen citoyen = citoyenRepository.findById(citoyenId)
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable"));
        return preferenceRepository.findByCitoyen(citoyen);
    }

    // ── NOTIFICATIONS IN-APP ──────────────────────────────────────

    public List<Notification> getNotifications(Long citoyenId) {
        Citoyen citoyen = citoyenRepository.findById(citoyenId)
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable"));
        return notificationRepository.findByCitoyenOrderByDateCreationDesc(citoyen);
    }

    public long getNbNonLues(Long citoyenId) {
        try {
            Citoyen citoyen = citoyenRepository.findById(citoyenId).orElse(null);
            if (citoyen == null) return 0;
            return notificationRepository.countByCitoyenAndLueFalse(citoyen);
        } catch (Exception e) {
            return 0;
        }
    }

    public void marquerToutesLues(Long citoyenId) {
        Citoyen citoyen = citoyenRepository.findById(citoyenId)
                .orElseThrow(() -> new RuntimeException("Citoyen introuvable"));
        notificationRepository.marquerToutesLues(citoyen);
    }

    // ── Email / SMS ───────────────────────────────────────────────
    private void envoyerEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to); msg.setSubject(subject); msg.setText(body);
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Erreur email: " + e.getMessage());
        }
    }

    private void envoyerSms(String to, String body) {
        if (twilioSid == null || twilioSid.isBlank()) return;
        try {
            String url  = "https://api.twilio.com/2010-04-01/Accounts/" + twilioSid + "/Messages.json";
            String auth = Base64.getEncoder().encodeToString((twilioSid + ":" + twilioToken).getBytes());
            String form = "From=" + java.net.URLEncoder.encode(twilioFrom, "UTF-8")
                        + "&To="  + java.net.URLEncoder.encode(to, "UTF-8")
                        + "&Body="+ java.net.URLEncoder.encode(body, "UTF-8");
            java.net.http.HttpClient.newHttpClient().send(
                java.net.http.HttpRequest.newBuilder().uri(java.net.URI.create(url))
                    .header("Authorization", "Basic " + auth)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(form)).build(),
                java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Erreur SMS: " + e.getMessage());
        }
    }
}