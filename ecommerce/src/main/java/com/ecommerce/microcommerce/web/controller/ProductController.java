package com.ecommerce.microcommerce.web.controller;


import com.ecommerce.microcommerce.web.Repository.ProductRepository;
import com.ecommerce.microcommerce.web.domain.Produit;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Api( description="API pour es opérations CRUD sur les produits.")
@RestController
public class ProductController {

    org.slf4j.Logger logger = LoggerFactory.getLogger(ProductController.class);


    @Autowired
    private ProductRepository p;

    @RequestMapping( value = "/Produits", method = RequestMethod.GET)
    @ApiOperation(value = "Récupère toute les produits!")
    public MappingJacksonValue listProduits(){
        logger.info("Listes les produits");
        return FilterData(p.findAll());
    }


    @GetMapping( value = "/Produits/{id}")
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    public Produit getProduit (@PathVariable Long id){
        logger.info("Find product with id :{}",id);
        Produit pp= p.findById(id);
        if (pp==null)
        throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

        return pp;
    }


    @PostMapping (value = "/Produits")
    @ApiOperation(value = "Ajouter un produit!")
    public ResponseEntity<Void> ajouterProduit (@RequestBody Produit produit){
        logger.info("add product with :{}",produit);
        Produit produitAjouter = p.save(produit);

         if (produitAjouter == null || produitAjouter.getPrix() == 0){
             return ResponseEntity.noContent().build();
         }



        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produitAjouter.getId())
                .toUri();


        return ResponseEntity.created(location).build();

    }



    @GetMapping (value = "test/produits/{prixLimit}")
    public List<Produit> testdesRequetes (@PathVariable Long prixLimit){
        logger.info("Find product with greater than limit price :{}",prixLimit);
        return p.chercherUnProduitCher(prixLimit);

    }

    @GetMapping(value = "produits/{recherche}")
    public List<Produit> test(@PathVariable String recherche) {
        logger.info("Find product with name like :{}",recherche);
        return p.findByNomLike("%"+recherche+"%");
    }



    @DeleteMapping (value = "/Produits/{id}")
    @ApiOperation(value = "Supprimer un produit!")
    public void supprimerProduit(@PathVariable Long id) {
        logger.info("Delete product with id :{}",id);
        p.delete(id);
    }



    @PutMapping (value = "/Produits")
    @ApiOperation(value = "Modifier un produit!")
    public void updateProduit(@RequestBody Produit product) {
        logger.info("Update product :{}",product);
        p.save(product);
    }







    public MappingJacksonValue FilterData (List<Produit> Produit_a_Filtrer){

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");

        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);

        MappingJacksonValue produitsFiltres = new MappingJacksonValue(Produit_a_Filtrer);

        produitsFiltres.setFilters(listDeNosFiltres);

        return produitsFiltres;
    }



}
