package com.example.ecommerceproject.Repository;

import com.example.ecommerceproject.Entity.User;
import com.example.ecommerceproject.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

   /* //  CUSTOM CREATE QUERY
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO User(name, email) VALUES(:name, :email)", nativeQuery = true)
    User create(@Param("name") String name, @Param("email") String email);

    // CUSTOM QUERY SELECT
    @Query("SELECT u FROM User u")
    List<User> getAll();

    @Query(value = "select * from User where name= 'umar001'",nativeQuery = true)
    List<User> getActive();
    //  CUSTOM QUERY  UPDATE

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name, u.email = :email WHERE u.id = :id")
    void update(@Param("id") Long id, @Param("name") String name, @Param("email") String email);

    // CUSTOM QUERY DELETE
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :id")
    void delete(@Param("id") Long id);
*/

   User findByEmail(String email);
}
