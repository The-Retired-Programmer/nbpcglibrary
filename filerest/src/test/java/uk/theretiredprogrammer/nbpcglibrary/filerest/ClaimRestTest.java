/*
 * Copyright 2017 richard.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.filerest;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author richard
 */
public class ClaimRestTest {
    
    public ClaimRestTest() {
    }
    
    /**
     * Test of getAuthEntityUser, of class UserRest.
     */
    @Test
    public void testGetAuthEntityClaim() {
        System.out.println("getAuthEntityClaim");
        ClaimRest cr = new ClaimRest();
        boolean isOpen = cr.open();
        assert(isOpen);
        ClaimEntity claim = cr.get(1);
        cr.close();
        assertNotNull(claim);
        assertEquals((long)claim.getId(),1);
        assertEquals(claim.getClaimkey(),"trp.acm.ok");
    }
    
    /**
     * Test of getAuthEntityUser, of class UserRest.
     */
    @Test
    public void testPostDelAuthEntityClaim() {
        System.out.println("postdelAuthEntityClaim");
        ClaimRest cr = new ClaimRest();
        boolean isOpen = cr.open();
        assert(isOpen);
        List<ClaimEntity> claims = cr.getAll();
        assertNotNull(claims);
        assertEquals(1, claims.size());
        // now get the claim record
        ClaimEntity claim = new ClaimEntity();
        claim.setClaimkey("trp.xxx.key");
        claim.setValue("valuexxx");
        claim.setUser(1);
        claim = cr.create(claim);
        assertNotNull(claim);
        assertEquals(claim.getValue(),"valuexxx");
        // get count of claim record
        List<ClaimEntity> claims2= cr.getAll();
        assertNotNull(claims2);
        assertEquals(2, claims2.size());
        ClaimEntity c1 = claims2.get(1);
        int id = c1.getId();
        boolean delresult = cr.delete(id);
        assertEquals(delresult, true);
        // get count of user record
        List<ClaimEntity> claims3= cr.getAll();
        cr.close();
        assertNotNull(claims3);
        assertEquals(1, claims3.size());
    }
    
    /**
     * Test of getMany, of class ClientRest.
     */
    @Test
    public void testGetManyClaim() {
        System.out.println("GetManyClaim");
        ClaimRest cr = new ClaimRest();
        boolean isOpen = cr.open();
        assert(isOpen);
        List<ClaimEntity> claims = cr.getMany("user",1);
        assertNotNull(claims);
        assertEquals(1, claims.size());
        assertEquals(1,(long)claims.get(0).getId());
        //
        claims = cr.getMany("user",2);
        cr.close();
        assertNotNull(claims);
        assertEquals(0, claims.size());
    }
}
