package com.hack.stock2u.product.repository;

import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.dto.ProductSummaryProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, Long> {
  List<Product> findTop5BySeller(User user);

  @SuppressWarnings("checkstyle:Indentation")
  @Query(value = """
SELECT DISTINCT
    p.id as id,
    p.title as title,
    p.price as price,
    p.type as productType,
    p.product_count as productCount,
    p.expired_at as expiredAt,
    (6371
        *ACOS(COS(RADIANS(:lat))
        *COS(RADIANS(sd.latitude))
        *COS(RADIANS(sd.longitude)-RADIANS(:lng))
        +SIN(RADIANS(:lat))*SIN(RADIANS(sd.latitude)))
    ) / 10 as distance,
    sd.latitude as latitude,
    sd.longitude as longitude,
    a.upload_path as thumbnailUrl,
    p.created_at as createdAt
FROM products p
LEFT JOIN users u on p.user_id = u.id
LEFT JOIN seller_details sd on u.seller_details_id = sd.id
LEFT JOIN attachments a ON a.id = (
    SELECT MIN(id) as id
    FROM attachments a2
    WHERE a2.user_id = p.user_id AND a2.product_id = p.id
    GROUP BY id
    LIMIT 1
)
WHERE p.removed_at IS NULL AND DATE(NOW()) < p.expired_at
  AND p.price BETWEEN :minPrice AND :maxPrice
  AND p.type in (:productTypes)
HAVING distance <= :distance
ORDER BY p.expired_at
LIMIT :pageSize
OFFSET :offset
  """, nativeQuery = true)
  List<ProductSummaryProjection> findProducts(
      Double lat,
      Double lng,
      List<String> productTypes,
      Double distance,
      Integer minPrice,
      Integer maxPrice,
      Integer pageSize,
      Long offset
  );


}
