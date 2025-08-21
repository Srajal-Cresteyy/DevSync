    package com.devsync_backend.securityconfig.repository;

    import com.devsync_backend.securityconfig.model.User;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface UserRepository extends JpaRepository<User,Integer> {
        public User findByUserName(String userName);
        public boolean existsByUserName(String userName);
    }
