package unit_test.service;

import io.github.bucket4j.Bucket;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

import com.example.authentication.service.RateLimiterService;

class RateLimiterServiceTest {

    @Test
    void testBucketCreationAndCaching() {
        RateLimiterService rateLimiterService = new RateLimiterService();

        Bucket bucket1 = rateLimiterService.resolveBucket("client1", 5, Duration.ofSeconds(10));
        Bucket bucket2 = rateLimiterService.resolveBucket("client1", 5, Duration.ofSeconds(10));

        assertNotNull(bucket1);
        assertSame(bucket1, bucket2); // Same cached bucket
    }
}
