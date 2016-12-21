package skyglass.data.model.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ACL {
  
    //==================================================================================================================
    // INSTANCE
    //==================================================================================================================
    private SecurityResource resource;
    private Set<Role> roleSet = new HashSet<Role>();
    private Set<DynamicRole> dynamicRoleSet = new HashSet<DynamicRole>();
    private Map<Role, Set<Group>> role2group = new HashMap<Role, Set<Group>>();
    private Map<Role, Set<User>> role2user = new HashMap<Role, Set<User>>();    
    
    public ACL(SecurityResource resource, Collection<UserRoleForResource> userRoles, 
    		Collection<GroupRoleForResource> groupRoles, Collection<DynamicRole> dynamicRoles) {
   
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.addAll(resource.getSchema().getRoles());
        
        Map<Role, Set<Group>> role2group = new HashMap<Role, Set<Group>>();
        Set<Group> groups = new HashSet<Group>();
        for (GroupRoleForResource groupRole : groupRoles) {
            Role role = groupRole.getRole();
            roleSet.add(role);
            
            Group group = groupRole.getGroup();
            groups.add(group);
            
            Set<Group> roleGroups = role2group.get(role);
            if (roleGroups == null) {
                roleGroups = new HashSet<Group>();
                role2group.put(role, roleGroups);
            }
            roleGroups.add(group);
        }

        Map<Role, Set<User>> role2user = new HashMap<Role, Set<User>>();
        Set<User> users = new HashSet<User>();
        for (UserRoleForResource userRole : userRoles) {
            Role role = userRole.getRole();
            roleSet.add(role);
            
            User user = userRole.getUser();
            users.add(user);
            
            Set<User> roleUsers = role2user.get(role);
            if (roleUsers == null) {
                roleUsers = new HashSet<User>();
                role2user.put(role, roleUsers);
            }
            roleUsers.add(user);
        }
        
        for (Role role : roleSet) {
            Set<Group> roleGroups = role2group.get(role);
            if (roleGroups != null) {
                for (Group roleGroup : roleGroups) {
                    groups.add(roleGroup);
                }
            }

            Set<User> roleUsers = role2user.get(role);
            if (roleUsers != null) {
                for (User roleUser : roleUsers) {
                    users.add(roleUser);
                }
            }
        }

        Set<DynamicRole> dynamicRoleSet = new HashSet<DynamicRole>();
       
        for (DynamicRole role: dynamicRoles) {
            dynamicRoleSet.add(role);
        }
        
        this.resource = resource;
        this.roleSet = roleSet;
        this.dynamicRoleSet = dynamicRoleSet;
        this.role2group = role2group;
        this.role2user = role2user;
    }

    public SecurityResource getResource() {
        return resource;
    }
    
    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roleSet);
    }
    
    public Set<DynamicRole> getDynamicRoles() {
        return Collections.unmodifiableSet(dynamicRoleSet);
    }
    
    public Set<Group> getGroupsInRole(Role role) {
        Set<Group> groups = role2group.get(role);
        return groups == null ? new HashSet<Group>() : Collections.unmodifiableSet(groups);
    }
    
    public Set<User> getUsersInRole(Role role) {
        Set<User> users = role2user.get(role);
        return users == null ? new HashSet<User>() : Collections.unmodifiableSet(users);
    }    
   
    @Override
    public int hashCode() {
        return resource.getId() != null ? resource.getId().hashCode() : super.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        if (obj != null && obj.getClass().equals(getClass())) {
            equal = this == obj || hashCode() == obj.hashCode();
        }
        return equal;
    }

}
