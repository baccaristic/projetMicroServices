package tn.esprit.pidev.bns.repository.bns;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidev.bns.entity.bns.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

}