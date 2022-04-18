package ma.enset.etudiantmvc.web;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.enset.etudiantmvc.entities.Etudiant;
import ma.enset.etudiantmvc.repositories.EtudiantRepository;
import ma.enset.etudiantmvc.security.entities.AppRole;
import ma.enset.etudiantmvc.security.entities.AppUser;
import ma.enset.etudiantmvc.security.repositories.AppRoleRepository;
import ma.enset.etudiantmvc.security.repositories.AppUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
@Controller
@Data @AllArgsConstructor
public class EtudiantController {
    private EtudiantRepository etudiantRepository;
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;

    @GetMapping(path = "/user/index")
    public String patients(Model model,
                           //param d'url
                           @RequestParam(name="page", defaultValue = "0") int page,
                           @RequestParam(name="size", defaultValue = "5") int size,
                           @RequestParam(name="keyword", defaultValue = "") String keyword){
        Page<Etudiant> pageEtudiant = etudiantRepository.findByNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("listEtudiant",pageEtudiant.getContent());
        model.addAttribute("pages",new int[pageEtudiant.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "etudiants";
    }
    @GetMapping("/admin/delete")
    public String delete(Long id, String keyword, int page){
        etudiantRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/user/etudiants")
    @ResponseBody
    public List<Etudiant> listPatients(){
        return etudiantRepository.findAll();
    }

    @GetMapping("/admin/formEtudiant")
    public String formEtudiant(Model model){
        String keyword ="";
        int page = 0;
        model.addAttribute("etudiant",new Etudiant());
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        return "formEtudiant";
    }
    @GetMapping("/user/profile")
    public String profile(Model model, String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        List<AppRole> roles = new ArrayList<>();
        for (AppRole role: appUser.getAppRoles()) {
            roles.add(role);
        }
        model.addAttribute("roles",roles);
        model.addAttribute("user",appUser);
        return "profile";
    }
    @PostMapping("/admin/save")
    //@Valid ==> je dis à spring mvc une fois tu fais l'ajout d'un patient au BDD tu fais la validation
    //si jamais il y a des erreurs tu les stockes dans BindingResult
    public String save(Model model, @Valid Etudiant etudiant, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "")String keyword){
        if (bindingResult.hasErrors())
            return "formEtudiant";
        etudiantRepository.save(etudiant);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/admin/editEtudiant")
    public String editPatient(Model model,Long id,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page){
        Etudiant etudiant = etudiantRepository.findById(id).orElse(null);
        if (etudiant==null) throw new RuntimeException("Etudiant introuvable!!!");
        model.addAttribute("etudiant",etudiant);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        return "editEtudiant";
    }

    @GetMapping("/user/listEtudiant")
    public String listPatient(Model model, Long id,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page){
        Etudiant etudiant = etudiantRepository.findById(id).get();
        model.addAttribute("etudiant",etudiant);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        return "listEtudiant";
    }
}
