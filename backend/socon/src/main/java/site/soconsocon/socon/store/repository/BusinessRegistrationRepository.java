package site.soconsocon.socon.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.soconsocon.socon.store.domain.entity.jpa.BusinessRegistration;

public interface BusinessRegistrationRepository extends JpaRepository<BusinessRegistration, Integer> {


}
