package com.hack.stock2u.product.repository;

import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.dto.ProductSummaryProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    WHERE a2.id = p.user_id AND a2.product_id = p.id
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
      @Param("lat") Double lat,
      @Param("lng") Double lng,
      @Param("productTypes") List<String> productTypes,
      @Param("distance") Double distance,
      @Param("minPrice") Integer minPrice,
      @Param("maxPrice") Integer maxPrice,
      @Param("pageSize") Integer pageSize,
      @Param("offset") Long offset
  );

  @SuppressWarnings("checkstyle:Indentation")
  @Query(value = """
select *
from (SELECT DISTINCT COUNT(p.id) as totalCount,
                      (6371
                          * ACOS(COS(RADIANS(33.450701))
                                     * COS(RADIANS(33.450701))
                                     * COS(RADIANS(126.570667) - RADIANS(126.570667))
                              + SIN(RADIANS(33.450701)) * SIN(RADIANS(33.450701)))
                          ) / 10  as distance
      FROM products p
               LEFT JOIN users u on p.user_id = u.id
               LEFT JOIN seller_details sd on u.seller_details_id = sd.id
      WHERE p.removed_at IS NULL
        AND DATE(NOW()) < p.expired_at
        AND p.price BETWEEN 0 AND 999999
        AND p.type in ('FOOD')
      HAVING distance <= 10) tCd
  """, nativeQuery = true)
  Long getCount(
      @Param("lat") Double lat,
      @Param("lng") Double lng,
      @Param("productTypes") List<String> productTypes,
      @Param("distance") Double distance,
      @Param("minPrice") Integer minPrice,
      @Param("maxPrice") Integer maxPrice
  );

}
